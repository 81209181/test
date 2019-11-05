package com.hkt.btu.sd.facade.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hkt.btu.common.facade.data.DataInterface;
import com.hkt.btu.sd.core.service.SdApiService;
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
        final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        WfmAppointmentResData responseData;
        try {
            responseData = getData("/api/v1/sd/GetAppointmentByTicketMasId/" + ticketMasId, WfmAppointmentResData.class, null);
        } catch (RuntimeException e) {
            LOG.error(String.format("WFM Error: Cannot get appointment info from WFM of %s.", ticketMasId));
            return null;
        }

        if (responseData == null) {
            LOG.error("WFM Error: No response");
            return null;
        }

        // appointmentDate format
        String appointmentDate = StringUtils.isEmpty(responseData.getAppointmentDate()) ? null :
                LocalDateTime.parse(responseData.getAppointmentDate(), DATE_TIME_FORMATTER).toLocalDate().toString();
        String appointmentStartTime = StringUtils.isEmpty(responseData.getAppointmentStartDateTime()) ? null :
                LocalDateTime.parse(responseData.getAppointmentStartDateTime(), DATE_TIME_FORMATTER).toLocalTime().toString();
        String appointmentEndTime = StringUtils.isEmpty(responseData.getAppointmentEndDateTime()) ? null :
                LocalDateTime.parse(responseData.getAppointmentEndDateTime(), DATE_TIME_FORMATTER).toLocalTime().toString();

        responseData.setAppointmentDate(appointmentDate);
        responseData.setAppointmentStartDateTime(appointmentStartTime);
        responseData.setAppointmentEndDateTime(appointmentEndTime);

        return responseData;
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
}
