package com.hkt.btu.sd.facade.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hkt.btu.common.facade.data.DataInterface;
import com.hkt.btu.sd.core.service.SdApiService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeOfferMappingBean;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;
import com.hkt.btu.sd.core.util.JsonUtils;
import com.hkt.btu.sd.facade.AbstractRestfulApiFacade;
import com.hkt.btu.sd.facade.WfmApiFacade;
import com.hkt.btu.sd.facade.data.SdTicketData;
import com.hkt.btu.sd.facade.data.WfmJobDetailsData;
import com.hkt.btu.sd.facade.data.WfmResponseData;
import com.hkt.btu.sd.facade.data.wfm.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class WfmApiFacadeImpl extends AbstractRestfulApiFacade implements WfmApiFacade {
    private static final Logger LOG = LogManager.getLogger(WfmApiFacadeImpl.class);

    @Resource(name = "apiService")
    SdApiService apiService;

    @Override
    protected SiteInterfaceBean getTargetApiSiteInterfaceBean() {
        return apiService.getSiteInterfaceBean(SiteInterfaceBean.API_WFM.API_NAME);
    }

    @Override
    protected Invocation.Builder getInvocationBuilder(WebTarget webTarget) {
        SiteInterfaceBean siteInterfaceBean = getTargetApiSiteInterfaceBean();
        return webTarget.request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getBtuHeaderAuthKey(siteInterfaceBean));
    }

    @Override
    public Integer createJob(SdTicketData ticketData) throws JsonProcessingException {
        Response res = postEntity("/api/v1/sd/FaultCreate", Entity.entity(JsonUtils.getMapperFormatLocalDateTime2String().writeValueAsString(ticketData), MediaType.APPLICATION_JSON));
        WfmResponse wfmRes = res.readEntity(WfmResponse.class);
        return Optional.ofNullable(wfmRes.getData()).map(WfmSuccess::getJobId).orElseThrow(() ->
                new RuntimeException(String.format("WFM Error: %s . Please contact WFM Admin.", wfmRes.getErrorMsg())));
    }

    @Override
    public boolean closeTicket(Integer ticketMasId) {
        try {
            return Optional.ofNullable(ticketMasId).flatMap(id -> Optional.ofNullable(postData("/api/v1/sd/CloseTicket/" + id, null, null))
                    .filter(StringUtils::isNotBlank)
                    .map(s -> StringUtils.containsIgnoreCase(s, "Success"))).orElse(false);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public WfmJobDetailsData getJobDetails(Integer jobId) {
        return Optional.ofNullable(jobId).map(id -> {
            Map<String, String> queryParamMap = new HashMap<>();
            queryParamMap.put("jobId", id.toString());
            String wfmResponseDataJsonString = getData("/api/v1/sd/FaultView", queryParamMap);
            WfmResponseData<WfmJobDetailsData> wfmResponseData = populateWfmResponseData(
                    wfmResponseDataJsonString, new TypeToken<WfmResponseData<WfmJobDetailsData>>() {
                    }.getType());
            return wfmResponseData.getData();
        }).orElse(new WfmJobDetailsData());
    }

    @Override
    public List<SdServiceTypeOfferMappingBean> getServiceTypeOfferMapping() {
        return Optional.ofNullable(new Gson().<List<WfmOfferNameProductTypeData>>fromJson(getData("/api/v1/sd/GetOfferNameProductTypeMapping", null),
                new TypeToken<List<WfmOfferNameProductTypeData>>() {
                }.getType()))
                .filter(CollectionUtils::isNotEmpty).map(list -> list.stream().map(data -> {
                    SdServiceTypeOfferMappingBean bean = new SdServiceTypeOfferMappingBean();
                    bean.setOfferName(data.getServiceName());
                    bean.setServiceTypeCode(data.getProductType());
                    return bean;
                }).collect(Collectors.toList())).orElseThrow(() -> new RuntimeException("Service type offer mapping not found."));
    }

    private <T extends DataInterface> WfmResponseData<T> populateWfmResponseData(String wfmResponseDataJsonString, Type type) {
        WfmResponseData<T> wfmResponseData = new Gson().fromJson(wfmResponseDataJsonString, type);
        Optional.ofNullable(wfmResponseData).ifPresent(data -> {
            LOG.info("Response type: " + data.getType());
            if (StringUtils.isNotEmpty(data.getErrorMsg())) {
                LOG.error("Response status: " + data.getCode());
                LOG.error("Error message: " + data.getErrorMsg());
            }
        });
        return wfmResponseData;
    }

    @Override
    public WfmPendingOrderData getPendingOrderByBsn(String bsn) {
        final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        WfmPendingOrderData responseData;
        try {
            responseData = getData("/api/v1/sd/GetPendingOrderByBsn/" + bsn, WfmPendingOrderData.class, null);
        } catch (RuntimeException e) {
            String errorMsg = String.format("WFM Error: Cannot check pending order from WFM of BSN %s.", bsn);
            LOG.error(errorMsg);

            responseData = new WfmPendingOrderData();
            responseData.setErrorMsg(errorMsg);
            return responseData;
        }

        if (responseData == null) {
            String errorMsg = "WFM Error: No response";
            LOG.error(errorMsg);

            responseData = new WfmPendingOrderData();
            responseData.setErrorMsg(errorMsg);
            return responseData;
        }

        // serviceReadyDate format
        String serviceReadyDate = StringUtils.isEmpty(responseData.getSrdStartDateTime()) ? null :
                LocalDateTime.parse(responseData.getSrdStartDateTime(), DATE_TIME_FORMATTER).toLocalDate().toString();
        String srdStartTime = StringUtils.isEmpty(responseData.getSrdStartDateTime()) ? "" :
                LocalDateTime.parse(responseData.getSrdStartDateTime(), DATE_TIME_FORMATTER).toLocalTime().toString();
        String srdEndTime = StringUtils.isEmpty(responseData.getSrdEndDateTime()) ? "" :
                LocalDateTime.parse(responseData.getSrdEndDateTime(), DATE_TIME_FORMATTER).toLocalTime().toString();
        if (!srdStartTime.equals("00:00") && !srdEndTime.equals("00:00")) {
            serviceReadyDate = serviceReadyDate == null ? null : serviceReadyDate +
                    (StringUtils.isEmpty(srdStartTime) ? StringUtils.EMPTY : StringUtils.SPACE + srdStartTime +
                            (StringUtils.isEmpty(srdEndTime) ? StringUtils.EMPTY : "-" + srdEndTime));
        }

        // appointmentDate format
        String appointmentDate = StringUtils.isEmpty(responseData.getSrdStartDateTime()) ? null :
                LocalDateTime.parse(responseData.getAppointmentDate(), DATE_TIME_FORMATTER).toLocalDate().toString();
        String appointmentStartTime = StringUtils.isEmpty(responseData.getAppointmentStartDateTime()) ? "" :
                LocalDateTime.parse(responseData.getAppointmentStartDateTime(), DATE_TIME_FORMATTER).toLocalTime().toString();
        String appointmentEndTime = StringUtils.isEmpty(responseData.getAppointmentEndDateTime()) ? "" :
                LocalDateTime.parse(responseData.getAppointmentEndDateTime(), DATE_TIME_FORMATTER).toLocalTime().toString();
        if (!appointmentStartTime.equals("00:00") && !appointmentEndTime.equals("00:00")) {
            appointmentDate = appointmentDate == null ? null : appointmentDate +
                    ( StringUtils.isEmpty(appointmentStartTime) ? StringUtils.EMPTY : StringUtils.SPACE + appointmentStartTime +
                            (StringUtils.isEmpty(appointmentEndTime) ? StringUtils.EMPTY : "-" + appointmentEndTime));
        }

        responseData.setServiceReadyDate(serviceReadyDate);
        responseData.setAppointmentDate(appointmentDate);

        return responseData;
    }

    @Override
    public List<WfmJobData> getJobInfo(Integer ticketMasId) {
        return Optional.ofNullable(ticketMasId).map(id -> {
            Type type = new TypeToken<List<WfmJobData>>() {
            }.getType();
            return this.<WfmJobData>getDataList("/api/v1/sd/GetJobListByTicketId/" + ticketMasId, type, null);
        }).orElse(null);
    }

    @Override
    public WfmAppointmentResData getAppointmentInfo(Integer ticketMasId) {
        try {
            return getData("/api/v1/sd/GetAppointmentByTicketMasId/" + ticketMasId,
                    WfmAppointmentResData.class, null);
        } catch (RuntimeException e) {
            LOG.error(String.format("WFM Error: Cannot get appointment info from WFM of %s.", ticketMasId));
            LOG.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<WfmJobProgressData> getJobProgessByTicketId(Integer ticketMasId) {
        return Optional.ofNullable(ticketMasId).map(id -> {
            Type type = new TypeToken<List<WfmJobProgressData>>() {}.getType();
            return this.<WfmJobProgressData>getDataList("/api/v1/sd/GetJobProgessByTicketId/" + ticketMasId, type, null);
        }).orElse(null);
    }

    @Override
    public List<WfmJobRemarksData> getJobRemarkByTicketId(Integer ticketMasId) {
        return Optional.ofNullable(ticketMasId).map(id -> {
            Type type = new TypeToken<List<WfmJobRemarksData>>() {}.getType();
            return this.<WfmJobRemarksData>getDataList("/api/v1/sd/GetJobRemarkByTicketId/" + ticketMasId, type, null);
        }).orElse(null);
    }

    @Override
    public String postAppointmentForm() {
        Form form = new Form();
        form.param("sdSystemParam.ticketMasId", "1061");
        form.param("sdSystemParam.ticketDetId", "662");
        form.param("sdSystemParam.symptomCode", "IF001");
        form.param("sdSystemParam.userName", "sd");
        form.param("sdSystemParam.password", "Ki6=rEDs47*^5");


        // response for test
        String response = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "\n" +
                "<title>WFM</title>\n" +
                "\n" +
                "<link href=\"https://10.252.15.158/wfm/css/re-render/bootstrap.min.css\" rel=\"stylesheet\">\n" +
                "<!-- Optional theme -->\n" +
                "<link href=\"https://10.252.15.158/wfm/css/re-render/bootstrap-theme.min.css\" rel=\"stylesheet\">\n" +
                "<!-- <link href=“https://10.252.15.158/wfm/css/re-render/demo/css/demo.css\" rel=\"stylesheet\">-->\n" +
                "<!-- Yamm styles (For main menu) -->\n" +
                "<link href=\"https://10.252.15.158/wfm/css/re-render/yamm/yamm.css\" rel=\"stylesheet\">\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "        <script type=\"text/javascript\" src=\"https://10.252.15.158/wfm/struts/js/base/jquery-2.2.4.min.js\"></script>\n" +
                "        <!-- script type=\"text/javascript\" src=\"https://10.252.15.158/wfm/struts/js/base/core.min.js?s2j=4.0.2\"></script -->\n" +
                "        <script type=\"text/javascript\" src=\"https://10.252.15.158/wfm/struts/js/base/version.min.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"https://10.252.15.158/wfm/struts/js/plugins/jquery.subscribe.min.js?s2j=4.0.2\"></script>\n" +
                "\n" +
                "<script type=\"text/javascript\" src=\"https://10.252.15.158/wfm/struts/js/struts2/jquery.struts2.min.js?s2j=4.0.2\"></script>\n" +
                "\n" +
                "<script type=\"text/javascript\">\n" +
                "    $(function () {\n" +
                "        jQuery.struts2_jquery.version = \"4.0.2\";\n" +
                "        jQuery.scriptPath = \"/wfm/struts/\";\n" +
                "        jQuery.struts2_jquery.gridLocal = \"en\";\n" +
                "        jQuery.struts2_jquery.timeLocal = \"en\";\n" +
                "        jQuery.struts2_jquery.datatablesLocal = \"en\";\n" +
                "        jQuery.ajaxSettings.traditional = true;\n" +
                "\n" +
                "        jQuery.ajaxSetup({\n" +
                "            cache: false\n" +
                "        });\n" +
                "\n" +
                "        jQuery.struts2_jquery.require(\"js/struts2/jquery.ui.struts2.min.js?s2j=4.0.2\");\n" +
                "\n" +
                "    });\n" +
                "</script>\n" +
                "\n" +
                "    <link id=\"jquery_theme_link\" rel=\"stylesheet\"\n" +
                "          href=\"https://10.252.15.158/wfm/struts/themes/smoothness/jquery-ui.css?s2j=4.0.2\" type=\"text/css\"/>\n" +
                "\n" +
                "<link rel=\"stylesheet\" href=\"https://10.252.15.158/wfm/css/re-render/jquery-ui.css\">\n" +
                "<!-- <link rel=\"stylesheet\" href=\"https://10.252.15.158https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css\">-->\n" +
                "\n" +
                "<!-- <link rel=\"stylesheet\"\n" +
                "\thref=\"https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css\">\n" +
                "<script src=\"https://code.jquery.com/jquery-1.12.4.js\"></script>\n" +
                "<script src=\"https://code.jquery.com/ui/1.12.1/jquery-ui.js\"></script> -->\n" +
                "<script src=\"https://10.252.15.158/wfm/js/jquery-ui.min.js\"></script>\n" +
                "\n" +
                "<script src=\"https://10.252.15.158/wfm/js/re-render/bootstrap.min.js\"></script>\n" +
                "<script src=\"https://10.252.15.158/wfm/js/re-render/util.js\"></script>\n" +
                "\n" +
                "<script src=\"https://10.252.15.158/wfm/js/TableMaint.js\"></script>\n" +
                "<SCRIPT src=\"https://10.252.15.158/wfm/js/common.js\"></script>\n" +
                "<script src=\"https://10.252.15.158/wfm/js/global.js\"></script>\n" +
                "\n" +
                "<link href=\"https://10.252.15.158/wfm/css/re-render/jquery-loading.css\" rel=\"stylesheet\">\n" +
                "<script src=\"https://10.252.15.158/wfm/js/re-render/jquery-loading.js\"></script>\n" +
                "\n" +
                "<link href=\"https://10.252.15.158/wfm/css/re-render/bootstrap-select.min.css\" rel=\"stylesheet\">\n" +
                "<script src=\"https://10.252.15.158/wfm/js/re-render/bootstrap-select.min.js\"></script>\n" +
                "\n" +
                "<link href=\"https://10.252.15.158/wfm/css/re-render/sticky-footer.css?v=2\" rel=\"stylesheet\">\n" +
                "\n" +
                "<style>\n" +
                ".js-loading-overlay {\n" +
                "\tposition: fixed;\n" +
                "\tleft: 0px;\n" +
                "\ttop: 0px;\n" +
                "\twidth: 100%;\n" +
                "\theight: 100%;\n" +
                "\tz-index: 9999;\n" +
                "\tbackground: url('/wfm/images/ring.svg')\n" +
                "\t\t50% 50% no-repeat;\n" +
                "\tbackground-color: rgba(0, 0, 0, 0.4);\n" +
                "}\n" +
                "\n" +
                ".required {\n" +
                "\tcolor: red;\n" +
                "}\n" +
                "</style>\n" +
                "\n" +
                "<script>\n" +
                "\t/*\n" +
                "\t for the datepicker cannot be trigger inside the jsp issue\n" +
                "\t http://stackoverflow.com/questions/29685927/datepicker-not-working-when-included-in-another-jsp-file\n" +
                "\t */\n" +
                "\t$(function() {\n" +
                "\t\t/*\n" +
                "\t\tselect picker issue in\n" +
                "\t\thttps://github.com/silviomoreto/bootstrap-select/issues/150\n" +
                "\t\t\thttps://stackoverflow.com/questions/41883537/how-to-reset-value-of-bootstrap-select-after-button-click\n" +
                "\t\t */\n" +
                "\t\t$(':reset').on('click', function(evt) {\n" +
                "\t\t\tevt.preventDefault();\n" +
                "\t\t\t$(\".selectpicker\").val('default');\n" +
                "\t\t\t$(\".selectpicker\").selectpicker(\"refresh\");\n" +
                "\t\t\t$(\".form-control\").val('');\n" +
                "\t\t});\n" +
                "\t});\n" +
                "</script>\n" +
                "<SCRIPT>\n" +
                "\t\n" +
                "\tfunction faptsubmit2() {\n" +
                "\t\talert(\"Appointment made successfully\");\n" +
                "\t\twindow.close();\n" +
                "\t\treturn false;\n" +
                "\t}\n" +
                "\t\n" +
                "\tfunction faptsubmit() {\n" +
                "\t\t//if (frmApptChart.apptDate.value != \"\" && frmApptChart.startTime.value != \"\" && frmApptChart.endTime.value != \"\")\n" +
                "\t\tif (frmApptChart.startTime.value != \"\" && frmApptChart.endTime.value != \"\")\n" +
                "\t\t{\t\n" +
                "\t\t\tvar inputData = '{&quot;staffId&quot;:&quot;&quot;,&quot;jobId&quot;:0,&quot;ticketMasId&quot;:71,&quot;ticketDetId&quot;:45,&quot;userName&quot;:&quot;sd&quot;,&quot;password&quot;:&quot;Ki6=rEDs47*^5&quot;,&quot;serviceType&quot;:&quot;&quot;,&quot;symptomCode&quot;:&quot;ABC123&quot;,&quot;appointmentDate&quot;:null,&quot;startTime&quot;:null,&quot;endTime&quot;:null,&quot;timeSlot&quot;:null}'.replace(new RegExp(\"&quot;\", 'g'),\"\\\"\");\n" +
                "\t\t\tinputData = addValueForName(inputData, 'appointmentDate', $(\"#apptDate\").val(), true);\n" +
                "\t\t\tinputData = addValueForName(inputData, 'startTime', $(\"#startTime\").val(), true);\n" +
                "\t\t\tinputData = addValueForName(inputData, 'endTime', $(\"#endTime\").val(), true);//alert(inputData);\n" +
                "\n" +
                "\t\t\tloading(function() {//alert('{&quot;staffId&quot;:&quot;&quot;,&quot;jobId&quot;:0,&quot;ticketMasId&quot;:71,&quot;ticketDetId&quot;:45,&quot;userName&quot;:&quot;sd&quot;,&quot;password&quot;:&quot;Ki6=rEDs47*^5&quot;,&quot;serviceType&quot;:&quot;&quot;,&quot;symptomCode&quot;:&quot;ABC123&quot;,&quot;appointmentDate&quot;:null,&quot;startTime&quot;:null,&quot;endTime&quot;:null,&quot;timeSlot&quot;:null}'.replace(new RegExp(\"&quot;\", 'g'),\"\\\"\"));\n" +
                "\t\t\t\t$.ajax({\n" +
                "\t\t\t\t\ttype : \"POST\", //Default is GET\n" +
                "\t\t\t\t\turl : \"../api/v1/sd/SdFaultCreate\", //URL you need to pass\n" +
                "\t\t\t\t\tdataType : \"json\",\n" +
                "\t\t\t\t\tcontentType : \"application/json; charset=utf-8\",\n" +
                "\t\t\t\t\tdata : JSON.stringify({\n" +
                "\t\t\t\t\t\t\tticketMasId : $(\"input[name='sdSystemParam.ticketMasId']\").val(),\n" +
                "\t\t\t\t\t\t\tticketDetId : $(\"input[name='sdSystemParam.ticketDetId']\").val(),\n" +
                "\t\t\t\t\t\t\tserviceType: $(\"input[name='sdSystemParam.serviceType']\").val(),\n" +
                "\t\t\t\t\t\t\tsymptomCode: $(\"input[name='sdSystemParam.symptomCode']\").val(),\n" +
                "\t\t\t\t\t\t\tappointmentDate : frmApptChart.apptDate.value,\n" +
                "\t\t\t\t\t\t\tstartTime : frmApptChart.startTime.value,\n" +
                "\t\t\t\t\t\t\tendTime : frmApptChart.endTime.value\n" +
                "\t\t\t\t\t\t}),\n" +
                "\t\t\t\t\tsuccess : function(value) {\n" +
                "\t\t\t\t\t\tif (value.data != null) {\n" +
                "\t\t\t\t\t\t\tvar result = value.data;\n" +
                "\t\t\t\t\t\t\tif( result.substr(0,1) == 'F' )\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\talert('Error : ' + result.substr(2) );\n" +
                "\t\t\t\t\t\t\t\treturn false;\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t$(\"#jobId\").val(result.substr(2));\n" +
                "\t\t\t\t\t\t}\n" +
                "\n" +
                "\t\t\t\t\t\tif ($(\"input[name=isOverbook]\").val() == \"Y\") {\n" +
                "\t\t\t\t\t\t\tvar overbookUrl = \"./JobAppointmentOverbookPublic.action\";\n" +
                "\t\t\t\t\t\t\tvar obj = $(\"form[name=frmApptChart]\")\n" +
                "\t\t\t\t\t\t\t\t\t.serializeArray();\n" +
                "\t\t\t\t\t\t\tvar str = Object.keys(obj).map(\n" +
                "\t\t\t\t\t\t\t\t\tfunction(key) {\n" +
                "\t\t\t\t\t\t\t\t\t\treturn obj[key].name + \"=\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t+ obj[key].value;\n" +
                "\t\t\t\t\t\t\t\t\t}).join('&');\n" +
                "\t\t\t\t\t\t\tlocation.href = overbookUrl + \"?\" + str;\n" +
                "\t\t\t\t\t\t} else {\n" +
                "\t\t\t\t\t\t\tapptBook(function(value) {\n" +
                "\t\t\t\t\t\t\t\tfinishLoading();\n" +
                "\t\t\t\t\t\t\t\talert(\"Appointment made successfully\");\n" +
                "\t\t\t\t\t\t\t\twindow.close();\n" +
                "\t\t\t\t\t\t\t}, function(xhr, ajaxOptions, thrownError) {\n" +
                "\t\t\t\t\t\t\t\t//$(\".errors\").html(\"Please Try Again\");\n" +
                "\t\t\t\t\t\t\t\talert(\"Error: \" + xhr.responseJSON.errorMsg);\n" +
                "\t\t\t\t\t\t\t\tdismissAll();\n" +
                "\t\t\t\t\t\t\t\tshowWarning(xhr.responseJSON.errorMsg);\n" +
                "\t\t\t\t\t\t\t\tfinishLoading();\n" +
                "\t\t\t\t\t\t\t});\n" +
                "\n" +
                "\t\t\t\t\t\t}\n" +
                "\n" +
                "\t\t\t\t\t\t//close and callback\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\terror : function(xhr, ajaxOptions, thrownError) {\n" +
                "\t\t\t\t\t\t//$(\".errors\").html(\"Please Try Again\");\n" +
                "\t\t\t\t\t\talert(\"Error: \" + xhr.responseJSON.errorMsg);\n" +
                "\t\t\t\t\t\tdismissAll();\n" +
                "\t\t\t\t\t\tshowWarning(xhr.responseJSON.errorMsg);\n" +
                "\t\t\t\t\t\tfinishLoading();\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t});\n" +
                "\t\t\t});\n" +
                "\t\t\treturn false;\n" +
                "\t\t} else\n" +
                "\t\t\t//alert(\"Please select appointment date and time. And also input the staff ID, SB No.\");\n" +
                "\t\t\talert(\"Please select Appointment Date and Time\");\n" +
                "\t\treturn false;\n" +
                "\t}\n" +
                "\t\n" +
                "\tfunction apptBook(successF, failureF) {\n" +
                "\t\tvar inputData = '{&quot;staffId&quot;:&quot;&quot;,&quot;jobId&quot;:0,&quot;ticketMasId&quot;:71,&quot;ticketDetId&quot;:45,&quot;userName&quot;:&quot;sd&quot;,&quot;password&quot;:&quot;Ki6=rEDs47*^5&quot;,&quot;serviceType&quot;:&quot;&quot;,&quot;symptomCode&quot;:&quot;ABC123&quot;,&quot;appointmentDate&quot;:null,&quot;startTime&quot;:null,&quot;endTime&quot;:null,&quot;timeSlot&quot;:null}'.replace(new RegExp(\"&quot;\", 'g'),\"\\\"\");\n" +
                "\t\tinputData = addValueForName(inputData, 'appointmentDate', $(\"#apptDate\").val(), true);\n" +
                "\t\tinputData = addValueForName(inputData, 'startTime', $(\"#startTime\").val(), true);\n" +
                "\t\tinputData = addValueForName(inputData, 'endTime', $(\"#endTime\").val(), true);\n" +
                "\t\tinputData = addValueForName(inputData, 'jobId', $(\"#jobId\").val(), false);\n" +
                "\t\tinputData = addValueForName(inputData, 'ticketMasId', $(\"input[name='sdSystemParam.ticketMasId']\").val(), false);\n" +
                "\t\tinputData = addValueForName(inputData, 'ticketDetId', $(\"input[name='sdSystemParam.ticketDetId']\").val(), false);//alert(inputData);\n" +
                "\n" +
                "\t\t$.ajax({\n" +
                "\t\t\ttype : \"POST\", //Default is GET\n" +
                "\t\t\turl : \"../api/v1/sd/JobAppointmentCreateGson\", //URL you need to pass\n" +
                "\t\t\tdataType : \"json\",\n" +
                "\t\t\tcontentType : \"application/json; charset=utf-8\",\n" +
                "\t\t\tdata : inputData,\n" +
                "\t\t\tsuccess : successF,\n" +
                "\t\t\terror : failureF\n" +
                "\t\t});\n" +
                "\t}\n" +
                "\n" +
                "\tDate.prototype.yyyymmdd = function() {\n" +
                "\t\tvar mm = this.getMonth() + 1; // getMonth() is zero-based\n" +
                "\t\tvar dd = this.getDate();\n" +
                "\n" +
                "\t\treturn [ this.getFullYear(), (mm > 9 ? '' : '0') + mm,\n" +
                "\t\t\t\t(dd > 9 ? '' : '0') + dd ].join('/');\n" +
                "\t};\n" +
                "</script>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t<div class=\"container-fluid\">\n" +
                "\t\t\n" +
                "<div class=\"alert alert-success\" role=\"alert\" style=\"display:none;\">\n" +
                "\t<span class=\"glyphicon glyphicon glyphicon-ok\" aria-hidden=\"true\"></span>\n" +
                "\t<span class=\"sr-only\">OK:</span><strong>OK!</strong>\n" +
                "\t<span id=\"msg\"></span>\n" +
                "</div>\n" +
                "<div class=\"alert alert-info\" role=\"alert\" style=\"display:none;\">\n" +
                "\t<span class=\"sr-only\">INFO:</span><strong>INFO!</strong>\n" +
                "\t<span id=\"msg\"></span>\n" +
                "</div>\n" +
                "<div class=\"alert alert-warning\" role=\"alert\" style=\"display:none;\">\n" +
                "\t<span class=\"sr-only\">WARNING:</span><strong>WARNING!</strong>\n" +
                "\t<span id=\"msg\"></span>\n" +
                "</div>\n" +
                "<div class=\"alert alert-danger\" role=\"alert\" style=\"display:none;\">\n" +
                "\t<span class=\"glyphicon glyphicon glyphicon-remove\" aria-hidden=\"true\"></span>\n" +
                "\t<span class=\"sr-only\">ERROR:</span><strong>ERROR!</strong>\n" +
                "\t<span id=\"msg\"></span>\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\t\t<!-- for the alert box -->\n" +
                "<div class=\"alert appt-alert-info alert-info\" role=\"alert\">\n" +
                "\t<span class=\"glyphicon glyphicon glyphicon-pencil\" aria-hidden=\"true\"></span>\n" +
                "\t<span class=\"sr-only\">Info:</span> <span class=\"msg\">Please\n" +
                "\t\tselect the available timeslot by <strong> clicking the column\n" +
                "\t\t\tor the time label in x-axis</strong>. You can also input custom time slot or\n" +
                "\t\texact time\n" +
                "\t</span>\n" +
                "</div>\n" +
                "<div class=\"alert appt-alert-success alert-success\" role=\"alert\"\n" +
                "\tstyle=\"display:none;\">\n" +
                "\t<span class=\"glyphicon glyphicon glyphicon-ok\" aria-hidden=\"true\"></span>\n" +
                "\t<span class=\"sr-only\">Success:</span> <span class=\"msg\">Default\n" +
                "\t\tSuccess Msg</span>\n" +
                "</div>\n" +
                "<div class=\"alert appt-alert-danger alert-danger\" role=\"alert\"\n" +
                "\tstyle=\"display:none;\">\n" +
                "\t<span class=\"glyphicon glyphicon-exclamation-sign\" aria-hidden=\"true\"></span>\n" +
                "\t<span class=\"sr-only\">Error:</span> <span class=\"msg\">Default\n" +
                "\t\tError Msg</span>\n" +
                "</div>\n" +
                "<div class=\"alert appt-alert-warning alert-warning\" role=\"alert\"\n" +
                "\tstyle=\"display:none;\">\n" +
                "\t<span class=\"glyphicon glyphicon-exclamation-sign\" aria-hidden=\"true\"></span>\n" +
                "\t<span class=\"sr-only\">Warning:</span> <span class=\"msg\">Default\n" +
                "\t\tWarning Msg</span>\n" +
                "</div>\n" +
                "\t\n" +
                "<!--     -->\n" +
                "<!--     -->\n" +
                "\n" +
                "<form id=\"#\" name=\"frmApptChart\" action=\"#\" method=\"get\" class=\"form-inline\">\n" +
                " <fieldset> \n" +
                "\n" +
                "\t\t\n" +
                "\n" +
                "\n" +
                "<script src=\"https://10.252.15.158/wfm/js/re-render/moment.js\"></script>\n" +
                "<script type=\"text/javascript\"\n" +
                "\tsrc=\"https://www.gstatic.com/charts/loader.js\"></script>\n" +
                "<script src=\"https://10.252.15.158/wfm/js/re-render/timeValidate.js\"></script>\n" +
                "<script\n" +
                "\tsrc=\"https://10.252.15.158/wfm/js/charts/sd/appointmentChart.js\"></script>\n" +
                "<script src=\"https://10.252.15.158/wfm/js/re-render/jquery-ui-1.12.1.custom/jquery-ui.js\"></script>\n" +
                "\n" +
                "\n" +
                "<div class=\"js-loading-overlay\"></div>\n" +
                "\n" +
                "\n" +
                "<!-- end for the alert box -->\n" +
                "<!-- for the graph -->\n" +
                "<div class=\"row\">\n" +
                "\t<div class=\"col-md-3 col-sm-4\">\n" +
                "\t\n" +
                "\t\t<div id=\"datepicker\"></div>\n" +
                "\t</div>\n" +
                "\t<div class=\"col-md-9 col-sm-8\">\n" +
                "\t\t<div id=\"barchart\" style=\"width: 100%; height: 200px;\"></div>\n" +
                "\t</div>\n" +
                "</div>\n" +
                "<!-- end for the graph -->\n" +
                "<!-- for the field group -->\n" +
                "<div class=\"row top-buffer\">\n" +
                "\t<input type=\"hidden\" id=\"apptDate\" name=\"apptDate\" value=\"\" />\n" +
                "\t<input type=\"hidden\" id=\"ticketMasId\" name=\"ticketMasId\" value=\"\" />\n" +
                "\t<input type=\"hidden\" id=\"ticketDetId\" name=\"ticketDetId\" value=\"\" />\n" +
                "\t<input type=\"hidden\" id=\"jobId\" name=\"jobId\" value=\"0\" />\n" +
                "\t<!-- s2b_form_element_class:  col-sm-2 -->\n" +
                "<!-- s2b_form_element_class:  col-sm-2 -->\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "<div class=\" \"><label class=\"col-sm-1 control-label\"            for=\"startTime\"             >From        </label>    <div class=\"col-sm-2\">\n" +
                "\n" +
                "<input type=\"text\" name=\"startTime\" maxlength=\"4\" value=\"\" id=\"startTime\" class=\"form-control\"/></div>\n" +
                "</div>\n" +
                "\n" +
                "\t<!-- s2b_form_element_class:  col-sm-2 -->\n" +
                "<!-- s2b_form_element_class:  col-sm-2 -->\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "<div class=\" \"><label class=\"col-sm-1 control-label\"            for=\"endTime\"             >To        </label>    <div class=\"col-sm-2\">\n" +
                "\n" +
                "<input type=\"text\" name=\"endTime\" maxlength=\"4\" value=\"\" id=\"endTime\" class=\"form-control\"/></div>\n" +
                "</div>\n" +
                "\n" +
                "\t<label for=\"isExactTime\">Exact Time &nbsp;<input type=\"checkbox\" name=\"isExactTime\" id=\"isExactTime\" placeholder=\"Is it Exact Time\"></label>\n" +
                "\t<button type=\"button\" class=\"btn btn-default\" onClick=\"reloadcal('');\">Week View</button>\n" +
                "</div>\n" +
                "<!-- /.modal -->\n" +
                "<div class=\"modal fade\" tabindex=\"-1\" role=\"dialog\"\n" +
                "\tid=\"alertModal\">\n" +
                "\t<div class=\"modal-dialog\" role=\"document\">\n" +
                "\t\t<div class=\"modal-content\">\n" +
                "\t\t\t<div class=\"modal-header\">\n" +
                "\t\t\t\t<button type=\"button\" class=\"close\" data-dismiss=\"modal\"\n" +
                "\t\t\t\t\taria-label=\"Close\">\n" +
                "\t\t\t\t\t<span aria-hidden=\"true\">&times;</span>\n" +
                "\t\t\t\t</button>\n" +
                "\t\t\t\t<h4 class=\"modal-title\">Warning</h4>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t<div class=\"modal-body\">\n" +
                "\t\t\t\t<p>The selected timeslot requires overbooking</p>\n" +
                "\t\t\t</div>\n" +
                "\t\t\t<div class=\"modal-footer\">\n" +
                "\t\t\t\t<button type=\"button\" class=\"btn btn-primary\" data-dismiss=\"modal\">Close</button>\n" +
                "\t\t\t</div>\n" +
                "\t\t</div>\n" +
                "\t\t<!-- /.modal-content -->\n" +
                "\t</div>\n" +
                "\t<!-- /.modal-dialog -->\n" +
                "</div>\n" +
                "<style type=\"text/css\">\n" +
                ".ui-widget-header {\n" +
                "\tborder: 1px solid #3366cc;\n" +
                "\tbackground: #76a7fa 50% 50% repeat-x;\n" +
                "\tcolor: #222222;\n" +
                "\tfont-weight: bold;\n" +
                "}\n" +
                "\n" +
                ".top-buffer {\n" +
                "\tmargin-top: 20px;\n" +
                "}\n" +
                "</style>\n" +
                "\n" +
                "\t\t<input type=\"hidden\" name=\"jobDur\" value=\"\">\n" +
                "\t\t<input type=\"hidden\" name=\"jobId\" value=\"\">\n" +
                "\t\t<!-- for service desk -->\n" +
                "\t\t<div class=\"row form-group col-md-12 top-buffer\">\n" +
                "\t\t\t<!-- s2b_form_element_class:  col-sm-2 -->\n" +
                "<!-- s2b_form_element_class:  col-sm-2 -->\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "<div class=\" \"><label class=\"col-sm-1 control-label\"            for=\"#_sdSystemParam_ticketMasId\"             >Ticket Master Id        </label>    <div class=\"col-sm-2\">\n" +
                "\n" +
                "<input type=\"text\" name=\"sdSystemParam.ticketMasId\" value=\"71\" disabled=\"disabled\" id=\"#_sdSystemParam_ticketMasId\" class=\"form-control\"/></div>\n" +
                "</div>\n" +
                "\n" +
                "\t\t\t<!-- s2b_form_element_class:  col-sm-2 -->\n" +
                "<!-- s2b_form_element_class:  col-sm-2 -->\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "<div class=\" \"><label class=\"col-sm-1 control-label\"            for=\"#_sdSystemParam_ticketDetId\"             >Ticket Detail Id        </label>    <div class=\"col-sm-2\">\n" +
                "\n" +
                "<input type=\"text\" name=\"sdSystemParam.ticketDetId\" value=\"45\" disabled=\"disabled\" id=\"#_sdSystemParam_ticketDetId\" class=\"form-control\"/></div>\n" +
                "</div>\n" +
                "\n" +
                "\t\t\t<!-- s2b_form_element_class:  col-sm-2 -->\n" +
                "<!-- s2b_form_element_class:  col-sm-2 -->\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "<div class=\" \"><label class=\"col-sm-1 control-label\"            for=\"#_sdSystemParam_serviceType\"             >Service Type        </label>    <div class=\"col-sm-2\">\n" +
                "\n" +
                "<input type=\"text\" name=\"sdSystemParam.serviceType\" value=\"\" disabled=\"disabled\" id=\"#_sdSystemParam_serviceType\" class=\"form-control\"/></div>\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "\t\t\t<button type=\"submit\" name=\"btnsubmit\"\n" +
                "\t\t\t\tclass=\"btn btn-primary text-right\" onClick=\"return faptsubmit();\">Submit</button>\n" +
                "\t\t</div>\n" +
                "\t\t<div class=\"row form-group col-md-12 top-buffer\">\n" +
                "\t\t\t<!-- s2b_form_element_class:  col-sm-2 -->\n" +
                "<!-- s2b_form_element_class:  col-sm-2 -->\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "<div class=\" \"><label class=\"col-sm-1 control-label\"            for=\"#_sdSystemParam_symptomCode\"             >Symptom Code        </label>    <div class=\"col-sm-2\">\n" +
                "\n" +
                "<input type=\"text\" name=\"sdSystemParam.symptomCode\" value=\"ABC123\" disabled=\"disabled\" id=\"#_sdSystemParam_symptomCode\" class=\"form-control\"/></div>\n" +
                "</div>\n" +
                "\n" +
                "\t\t</div>\n" +
                "\t </fieldset> </form>\n" +
                "\n" +
                "\n" +
                "<script type=\"text/javascript\">\n" +
                "    if (typeof jQuery != 'undefined') {\n" +
                "        if (typeof jQuery.fn.tooltip == 'function') {\n" +
                "            jQuery('i.s2b_tooltip').tooltip();\n" +
                "        }\n" +
                "    }\n" +
                "</script>\n" +
                "\n" +
                "\t<script>\n" +
                "\t\tvar staffId = \"\";\n" +
                "\t\tvar ticketMasId = \"71\";\n" +
                "\t\tvar ticketDetId = \"45\";\n" +
                "\t\tvar baseUrl = \"/wfm\";\n" +
                "\t\tvar jobDur = \"\";\n" +
                "\t\tvar date = new Date();\n" +
                "\t\tsetup({\n" +
                "\t\t\t/*dailyRes : {\n" +
                "\t\t\t\tresUrl : function(dateStr) {\n" +
                "\t\t\t\t\treturn baseUrl\n" +
                "\t\t\t\t\t\t\t+ \"/api/v1/sd/JobAppointmentDailyResGson?sdSystemParam.ticketMasId=\" + ticketMasId\n" +
                "\t\t\t\t\t\t\t+ \"&sdSystemParam.ticketDetId=\" + ticketDetId\n" +
                "\t\t\t\t\t\t\t+ \"&sdSystemParam.staffId=\" + staffId\n" +
                "\t\t\t\t\t\t\t+ \"&apptDate=\" + dateStr;\n" +
                "\t\t\t\t}\n" +
                "\t\t\t},*/\n" +
                "\t\t\tdailyRes : {\n" +
                "\t\t\t\tresUrl : function() {\n" +
                "\t\t\t\t\treturn baseUrl + \"/api/v1/sd/JobAppointmentDailyResGson\";\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\tresData : function(defaultAppointmentDate, appointmentDate) {\n" +
                "\t\t\t\t\tinputData = '{&quot;staffId&quot;:&quot;&quot;,&quot;jobId&quot;:0,&quot;ticketMasId&quot;:71,&quot;ticketDetId&quot;:45,&quot;userName&quot;:&quot;sd&quot;,&quot;password&quot;:&quot;Ki6=rEDs47*^5&quot;,&quot;serviceType&quot;:&quot;&quot;,&quot;symptomCode&quot;:&quot;ABC123&quot;,&quot;appointmentDate&quot;:null,&quot;startTime&quot;:null,&quot;endTime&quot;:null,&quot;timeSlot&quot;:null}'.replace(new RegExp(\"&quot;\", 'g'),\"\\\"\");\n" +
                "\t\t\t\t\tif( appointmentDate != '' && appointmentDate != 'undefined' )\n" +
                "\t\t\t\t\t\tinputData = addValueForName(inputData, 'appointmentDate', appointmentDate, true);\n" +
                "\t\t\t\t\t//alert(inputData);\n" +
                "\t\t\t\t\treturn inputData;\n" +
                "\t\t\t\t}\n" +
                "\t\t\t},\n" +
                "\t\t\tweeklyRes : {\n" +
                "\t\t\t\tresUrl : function() {\n" +
                "\t\t\t\t\treturn baseUrl\n" +
                "\t\t\t\t\t\t\t+ \"/api/v1/sd/JobAppointmentWeeklyResGson\";\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\tresData : function(defaultAppointmentDate, appointmentDate) {\n" +
                "\t\t\t\t\tinputData = '{&quot;staffId&quot;:&quot;&quot;,&quot;jobId&quot;:0,&quot;ticketMasId&quot;:71,&quot;ticketDetId&quot;:45,&quot;userName&quot;:&quot;sd&quot;,&quot;password&quot;:&quot;Ki6=rEDs47*^5&quot;,&quot;serviceType&quot;:&quot;&quot;,&quot;symptomCode&quot;:&quot;ABC123&quot;,&quot;appointmentDate&quot;:null,&quot;startTime&quot;:null,&quot;endTime&quot;:null,&quot;timeSlot&quot;:null}'.replace(new RegExp(\"&quot;\", 'g'),\"\\\"\");\n" +
                "\t\t\t\t\tif( appointmentDate != '' && appointmentDate != 'undefined' )\n" +
                "\t\t\t\t\t\tinputData = addValueForName(inputData, 'appointmentDate', appointmentDate, true);\n" +
                "\t\t\t\t\t//alert(inputData);\n" +
                "\t\t\t\t\treturn inputData;\n" +
                "\t\t\t\t}\n" +
                "\t\t\t},\n" +
                "\t\t\toverBookRes : {\n" +
                "\t\t\t\tresUrl : function() {\n" +
                "\t\t\t\t\treturn baseUrl + \"/api/v1/sd/JobAppointmentOverBookGson\";\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\tresData : function(timeSlot, defaultAppointmentDate, appointmentDate) {\n" +
                "\t\t\t\t\tinputData = '{&quot;staffId&quot;:&quot;&quot;,&quot;jobId&quot;:0,&quot;ticketMasId&quot;:71,&quot;ticketDetId&quot;:45,&quot;userName&quot;:&quot;sd&quot;,&quot;password&quot;:&quot;Ki6=rEDs47*^5&quot;,&quot;serviceType&quot;:&quot;&quot;,&quot;symptomCode&quot;:&quot;ABC123&quot;,&quot;appointmentDate&quot;:null,&quot;startTime&quot;:null,&quot;endTime&quot;:null,&quot;timeSlot&quot;:null}'.replace(new RegExp(\"&quot;\", 'g'),\"\\\"\");\n" +
                "\t\t\t\t\tif( timeSlot != '' && timeSlot != 'undefined' )\n" +
                "\t\t\t\t\t\tinputData = addValueForName(inputData, 'timeSlot', timeSlot, true);\n" +
                "\t\t\t\t\tif( appointmentDate != '' && appointmentDate != 'undefined' )\n" +
                "\t\t\t\t\t\tinputData = addValueForName(inputData, 'appointmentDate', appointmentDate, true);\n" +
                "\t\t\t\t\t//alert(inputData);\n" +
                "\t\t\t\t\treturn inputData;\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t});\n" +
                "\t\t\n" +
                "\t\tfunction addValueForName(stringData, name, value, isDoubleQuote)\n" +
                "\t\t{\t\n" +
                "\t\t\tvar start = 0;\n" +
                "\t\t\tvar end = 0;\n" +
                "\t\t\tstart = stringData.indexOf(name) + name.length + 2;//alert(stringData.substring(start, start + 1));\n" +
                "\t\t\tif( stringData.substring(start, start + 1) == '\"' )\n" +
                "\t\t\t{//alert('@1 ' + name);\n" +
                "\t\t\t\tend = stringData.indexOf(\"\\\"\", start + 1);\n" +
                "\t\t\t\tif( isDoubleQuote )\n" +
                "\t\t\t\t\treturn stringData.substring(0, start + 1) + value + stringData.substring(end);\n" +
                "\t\t\t\telse\n" +
                "\t\t\t\t\treturn stringData.substring(0, start) + value + stringData.substring(end + 1);\n" +
                "\t\t\t}\n" +
                "\t\t\telse\n" +
                "\t\t\t{\t//alert('@2 ' + name);\n" +
                "\t\t\t\tend = stringData.indexOf(\",\", start + 1);\n" +
                "\t\t\t\tif( end < 0 )\n" +
                "\t\t\t\t\tend = stringData.indexOf(\"}\", start + 1);\n" +
                "\t\t\t\tif( isDoubleQuote )\n" +
                "\t\t\t\t\treturn stringData.substring(0, start) + \"\\\"\" + value + \"\\\"\" + stringData.substring(end);\n" +
                "\t\t\t\telse\n" +
                "\t\t\t\t\treturn stringData.substring(0, start) + value + stringData.substring(end);\n" +
                "\t\t\t}\t\t\n" +
                "\t\t};\n" +
                "\t\t\n" +
                "\t\tfunction getValueForName(stringData, name)\n" +
                "\t\t{\t\n" +
                "\t\t\tvar start = 0;\n" +
                "\t\t\tvar end = 0;\n" +
                "\t\t\tstart = stringData.indexOf(name) + name.length + 2;//alert(stringData.substring(start, start + 4));\n" +
                "\t\t\tif( stringData.substring(start, start + 4) == 'null' )\n" +
                "\t\t\t{\n" +
                "\t\t\t\tend = start + 4;\n" +
                "\t\t\t\treturn 'null';\n" +
                "\t\t\t}\n" +
                "\t\t\telse\n" +
                "\t\t\t{\n" +
                "\t\t\t\tend = stringData.indexOf(\"\\\"\", start + 1);\n" +
                "\t\t\t\treturn stringData.substring(start, end);\n" +
                "\t\t\t}\n" +
                "\t\t};\n" +
                "\t</script>\n" +
                "\t</div>\n" +
                "</body>\n" +
                "</html>    ";

        return response;
    }
}
