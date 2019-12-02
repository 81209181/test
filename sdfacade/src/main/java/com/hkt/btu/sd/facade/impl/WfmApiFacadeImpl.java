package com.hkt.btu.sd.facade.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hkt.btu.sd.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.SdApiService;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeOfferMappingBean;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;
import com.hkt.btu.sd.core.util.JsonUtils;
import com.hkt.btu.sd.facade.AbstractRestfulApiFacade;
import com.hkt.btu.sd.facade.WfmApiFacade;
import com.hkt.btu.sd.facade.data.SdTicketData;
import com.hkt.btu.sd.facade.data.wfm.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import javax.ws.rs.NotFoundException;
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

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    private static final DateTimeFormatter DISPLAY_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
                new RuntimeException(String.format("WFM Error: %s. Please contact WFM Admin.", wfmRes.getErrorMsg())));
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

    @Override
    public WfmPendingOrderData getPendingOrderByBsn(String bsn) {

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

        // no pending order
        if (responseData == null) {
            responseData = new WfmPendingOrderData();
            return responseData;
        }

        // serviceReadyDate format
        String serviceReadyDate = dateStrFormat(responseData.getSrdStartDateTime(), null);
        String srdStartTime = dateStrFormat(responseData.getSrdStartDateTime(), null);
        String srdEndTime = dateStrFormat(responseData.getSrdEndDateTime(), null);
        if (!srdStartTime.equals("00:00") && !srdEndTime.equals("00:00")) {
            serviceReadyDate = serviceReadyDate == null ? null : serviceReadyDate +
                    (StringUtils.isEmpty(srdStartTime) ? StringUtils.EMPTY : StringUtils.SPACE + srdStartTime +
                            (StringUtils.isEmpty(srdEndTime) ? StringUtils.EMPTY : "-" + srdEndTime));
        }

        // appointmentDate format
        String appointmentDate = dateStrFormat(responseData.getAppointmentDate(), null);
        String appointmentStartTime = dateStrFormat(responseData.getAppointmentStartDateTime(), null);
        String appointmentEndTime = dateStrFormat(responseData.getAppointmentEndDateTime(), null);
        if (!appointmentStartTime.equals("00:00") && !appointmentEndTime.equals("00:00")) {
            appointmentDate = appointmentDate == null ? null : appointmentDate +
                    (StringUtils.isEmpty(appointmentStartTime) ? StringUtils.EMPTY : StringUtils.SPACE + appointmentStartTime +
                            (StringUtils.isEmpty(appointmentEndTime) ? StringUtils.EMPTY : "-" + appointmentEndTime));
        }

        responseData.setServiceReadyDate(serviceReadyDate);
        responseData.setAppointmentDate(appointmentDate);

        return responseData;
    }

    @Override
    public List<WfmJobData> getJobInfo(Integer ticketMasId) {
        return Optional.ofNullable(ticketMasId).map(id -> {
            Type type = new TypeToken<List<WfmJobData>>() {}.getType();
            List<WfmJobData> dataList = this.getDataList("/api/v1/sd/GetJobListByTicketId/" + ticketMasId, type, null);
            formatJobInfoDate(dataList);
            return dataList.stream().filter(jobInfo -> !WfmJobData.LOCKED_STATUS.equals(jobInfo.getStatus())).collect(Collectors.toList());
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
            Type type = new TypeToken<List<WfmJobProgressData>>() {
            }.getType();
            return this.<WfmJobProgressData>getDataList("/api/v1/sd/GetJobProgessByTicketId/" + ticketMasId, type, null);
        }).orElse(null);
    }

    @Override
    public List<WfmJobRemarksData> getJobRemarkByTicketId(Integer ticketMasId) {
        return Optional.ofNullable(ticketMasId).map(id -> {
            Type type = new TypeToken<List<WfmJobRemarksData>>() {
            }.getType();
            return this.<WfmJobRemarksData>getDataList("/api/v1/sd/GetJobRemarkByTicketId/" + ticketMasId, type, null);
        }).orElse(null);
    }

    @Override
    public WfmResponseTokenData getToken(WfmMakeApptData makeApptData) {
        String ticketMasId = String.valueOf(makeApptData.getTicketMasId());
        String ticketDetId = String.valueOf(makeApptData.getTicketDetId());
        String symptomCode = makeApptData.getSymptomCode();
        String serviceType = makeApptData.getServiceType();
        String bsn = String.valueOf(makeApptData.getBsn());

        // check input
        if (StringUtils.isEmpty(ticketMasId)) {
            throw new InvalidInputException("Ticket mas id is empty.");
        } else if (StringUtils.isEmpty(ticketDetId)) {
            throw new InvalidInputException("Ticket det id is empty.");
        } else if (StringUtils.isEmpty(symptomCode)) {
            throw new InvalidInputException("Symptom code is empty.");
        } else if (StringUtils.isEmpty(serviceType)) {
            throw new InvalidInputException("Service type is empty.");
        } else if (StringUtils.isEmpty(bsn)) {
            throw new InvalidInputException("Bsn is empty.");
        }

        // prepare param
        Map<String, String> queryParam = new HashMap<>(3);
        queryParam.put("ticketMasId", ticketMasId);
        queryParam.put("ticketDetId", ticketDetId);
        queryParam.put("symptomCode", symptomCode);
        queryParam.put("serviceType", serviceType);
        queryParam.put("bsn", bsn);

        // call WFM API
        try {
            String url = getTargetApiSiteInterfaceBean().getUrl();
            String jwt = getData("/api/v1/sd/token", queryParam);
            if (StringUtils.isEmpty(jwt)) {
                return null;
            }
            return WfmResponseTokenData.of(jwt, url);
        } catch (NotFoundException e) {
            LOG.warn(e.getMessage());
        }

        // failure return
        return null;
    }

    private String dateStrFormat(String time, DateTimeFormatter displayTimeFormatter) {
        return Optional.ofNullable(displayTimeFormatter)
                .map(formatter -> StringUtils.isEmpty(time) ? null :
                                   LocalDateTime.parse(time, DATE_TIME_FORMATTER).format(formatter))
                .orElse(StringUtils.isEmpty(time) ? null : LocalDateTime.parse(time, DATE_TIME_FORMATTER).toLocalDate().toString());
    }

    private void formatJobInfoDate(List<WfmJobData> dataList) {
        if (CollectionUtils.isNotEmpty(dataList)) {
            for (WfmJobData wfmJobData : dataList) {
                wfmJobData.setApptSTime(dateStrFormat(wfmJobData.getApptSTime(), DISPLAY_DATE_TIME_FORMATTER));
                wfmJobData.setApptETime(dateStrFormat(wfmJobData.getApptETime(), DISPLAY_DATE_TIME_FORMATTER));
                wfmJobData.setSrTimestamp(dateStrFormat(wfmJobData.getSrTimestamp(), DISPLAY_DATE_TIME_FORMATTER));
                wfmJobData.setApptTimestamp(dateStrFormat(wfmJobData.getApptTimestamp(), DISPLAY_DATE_TIME_FORMATTER));
                wfmJobData.setActionTimestamp(dateStrFormat(wfmJobData.getActionTimestamp(), DISPLAY_DATE_TIME_FORMATTER));
                wfmJobData.setLastUpTimestampTimestamp(dateStrFormat(wfmJobData.getLastUpTimestampTimestamp(), DISPLAY_DATE_TIME_FORMATTER));
            }
        }
    }
}
