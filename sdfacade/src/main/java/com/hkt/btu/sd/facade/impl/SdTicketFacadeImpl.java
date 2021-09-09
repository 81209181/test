package com.hkt.btu.sd.facade.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.facade.data.BtuCodeDescData;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.core.exception.ApiException;
import com.hkt.btu.sd.core.exception.AuthorityNotFoundException;
import com.hkt.btu.sd.core.service.SdTicketService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.*;
import com.hkt.btu.sd.core.service.constant.TicketStatusEnum;
import com.hkt.btu.sd.core.service.constant.TicketTypeEnum;
import com.hkt.btu.sd.core.util.POIExcelWriter;
import com.hkt.btu.sd.facade.*;
import com.hkt.btu.sd.facade.constant.OssTicketActionEnum;
import com.hkt.btu.sd.facade.constant.ServiceSearchEnum;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.data.bes.BesFaultInfoData;
import com.hkt.btu.sd.facade.data.bes.BesSubFaultData;
import com.hkt.btu.sd.facade.data.cloud.Attachment;
import com.hkt.btu.sd.facade.data.cloud.Attribute;
import com.hkt.btu.sd.facade.data.cloud.HktCloudCaseData;
import com.hkt.btu.sd.facade.data.cloud.HktCloudViewData;
import com.hkt.btu.sd.facade.data.gmb.GmbVehicleData;
import com.hkt.btu.sd.facade.data.gmb.Parameter;
import com.hkt.btu.sd.facade.data.oss.OssSmartMeterData;
import com.hkt.btu.sd.facade.data.wfm.*;
import com.hkt.btu.sd.facade.populator.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class SdTicketFacadeImpl implements SdTicketFacade {
    private static final Logger LOG = LogManager.getLogger(SdTicketFacadeImpl.class);

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private final static DateTimeFormatter DEFAULT_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FILE_TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private static final String XLS_SUFFIX = ".xls";

    @Resource(name = "ticketService")
    SdTicketService ticketService;
    @Resource(name = "auditTrailFacade")
    SdAuditTrailFacade auditTrailFacade;
    @Resource(name = "userService")
    SdUserService userService;
    @Resource(name = "userRoleFacade")
    SdUserRoleFacade userRoleFacade;
    @Resource(name = "wfmApiFacade")
    WfmApiFacade wfmApiFacade;
    @Resource(name = "serviceTypeFacade")
    SdServiceTypeFacade serviceTypeFacade;
    @Resource(name = "ossApiFacade")
    OssApiFacade ossApiFacade;
    @Resource(name = "smartMeterFacade")
    SdSmartMeterFacade smartMeterFacade;
    @Resource(name = "gmbApiFacade")
    GmbApiFacade gmbApiFacade;

    @Resource(name = "ticketMasDataPopulator")
    SdTicketMasDataPopulator ticketMasDataPopulator;
    @Resource(name = "ticketContactDataPopulator")
    SdTicketContactDataPopulator ticketContactDataPopulator;
    @Resource(name = "ticketServiceDataPopulator")
    SdTicketServiceDataPopulator ticketServiceDataPopulator;
    @Resource(name = "ticketRemarkDataPopulator")
    SdTicketRemarkDataPopulator ticketRemarkDataPopulator;
    @Resource(name = "faultInfoDataPopulator")
    BesFaultInfoDataPopulator faultInfoDataPopulator;
    @Resource(name = "teamSummaryDataPopulator")
    SdTeamSummaryDataPopulator teamSummaryDataPopulator;
    @Resource(name = "cloudViewDataPopulator")
    HktCloudViewDataPopulator cloudViewDataPopulator;
    @Resource(name = "ticketUploadFileDataPopulator")
    SdTicketUploadFileDataPopulator ticketUploadFileDataPopulator;
    @Resource(name = "closeCodeDataPopulator")
    SdCloseCodeDataPopulator sdCloseCodeDataPopulator;
    @Resource(name = "outstandingFaultDataPopulator")
    SdOutstandingFaultDataPopulator outstandingFaultDataPopulator;
    @Resource(name = "ticketTimePeriodSummaryDataPopulator")
    SdTicketTimePeriodSummaryDataPopulator ticketTimePeriodSummaryDataPopulator;


    @Override
    public int createQueryTicket(SdQueryTicketRequestData queryTicketRequestData) {
        if (!ServiceSearchEnum.TENANT_ID.getKey().equalsIgnoreCase(queryTicketRequestData.getSearchKey())
                && !ServiceSearchEnum.POLE_ID.getKey().equalsIgnoreCase(queryTicketRequestData.getSearchKey())
                && !ServiceSearchEnum.PLATE_ID.getKey().equalsIgnoreCase(queryTicketRequestData.getSearchKey()) ) {
            if (StringUtils.isBlank(queryTicketRequestData.getCustCode())) {
                throw new InvalidInputException("Customer Code is Empty.");
            }
        }

        int ticketMasId = ticketService.createQueryTicket(
                queryTicketRequestData.getCustCode(),
                queryTicketRequestData.getServiceNo(),
                queryTicketRequestData.getServiceType(),
                queryTicketRequestData.getSubsId(),
                queryTicketRequestData.getSearchKey(),
                queryTicketRequestData.getSearchValue(),
                queryTicketRequestData.getCustName());

        if (ServiceSearchEnum.PLATE_ID.getKey().equalsIgnoreCase(queryTicketRequestData.getSearchKey())) {
            GmbVehicleData vehicleData = gmbApiFacade.getVehicleInfo(queryTicketRequestData.getSearchValue());
            if (vehicleData != null) {
                ticketService.insertExtraInfo(ticketMasId, "PSL ID", vehicleData.getPslId());
                ticketService.insertExtraInfo(ticketMasId, "Route Code", vehicleData.getRouteCode());
            }
        }

        return ticketMasId;
    }

    @Override
    public Optional<SdTicketMasData> getTicket(Integer ticketId) {
        SdTicketMasData ticketMasData = new SdTicketMasData();
        return ticketService.getTicket(ticketId).map(sdTicketMasBean -> {
            ticketMasDataPopulator.populate(sdTicketMasBean, ticketMasData);
            auditTrailFacade.insertViewTicketAuditTrail(userService.getCurrentUserUserId(), String.valueOf(ticketMasData.getTicketMasId()));
            return ticketMasData;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateContactInfo(List<SdTicketContactData> contactList) {

        for (SdTicketContactData data : contactList) {
            if (StringUtils.isEmpty(data.getContactName())) {
                return "In contact type : " + data.getContactType() + ", please input contact name.";
            } else if (StringUtils.isEmpty(data.getContactNumber()) && StringUtils.isEmpty(data.getContactMobile()) && StringUtils.isEmpty(data.getContactEmail())) {
                return "In Contact Name : " + data.getContactName() + ", please input Contact No. or Contact Mobile or Contact Email, at least one is not empty.";
            }
        }

        try {
            ticketService.removeContactInfoByTicketMasId(contactList.get(0).getTicketMasId());
            contactList.forEach(data -> ticketService.insertTicketContactInfo(data.getTicketMasId(), data.getContactType(), data.getContactName(), data.getContactNumber(), data.getContactEmail(), data.getContactMobile()));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return "Update contact info failed.";
        }

        return null;
    }

    @Override
    public List<SdTicketContactData> getContactInfo(Integer ticketMasId) {
        List<SdTicketContactBean> beans = ticketService.getContactInfo(ticketMasId);
        List<SdTicketContactData> dataList = new ArrayList<>();
        beans.forEach(sdTicketContactBean -> {
            SdTicketContactData data = new SdTicketContactData();
            ticketContactDataPopulator.populate(sdTicketContactBean, data);
            dataList.add(data);
        });
        return dataList;
    }

    @Override
    public PageData<SdTicketMasData> searchTicketList(Pageable pageable, Map<String, String> searchFormData) {
        LocalDate createDateFrom = StringUtils.isEmpty(searchFormData.get("createDateFrom")) ? null : LocalDate.parse(searchFormData.get("createDateFrom"));
        LocalDate createDateTo = StringUtils.isEmpty(searchFormData.get("createDateTo")) ? null : LocalDate.parse(searchFormData.get("createDateTo"));

        String status = StringUtils.isEmpty(searchFormData.get("status")) ? null : searchFormData.get("status");
        LocalDate completeDateFrom = StringUtils.isEmpty(searchFormData.get("completeDateFrom")) ? null : LocalDate.parse(searchFormData.get("completeDateFrom"));
        LocalDate completeDateTo = StringUtils.isEmpty(searchFormData.get("completeDateTo")) ? null : LocalDate.parse(searchFormData.get("completeDateTo"));

        String createBy = StringUtils.isEmpty(searchFormData.get("createBy")) ? null : searchFormData.get("createBy");
        String ticketMasId = StringUtils.isEmpty(searchFormData.get("ticketMasId")) ? null : searchFormData.get("ticketMasId");
        String custCode = StringUtils.isEmpty(searchFormData.get("custCode")) ? null : searchFormData.get("custCode");

        String serviceNumber = StringUtils.isEmpty(searchFormData.get("serviceNumber")) ? null : searchFormData.get("serviceNumber");
        String serviceNumberExact = StringUtils.isEmpty(searchFormData.get("serviceNumberExact")) ? null : searchFormData.get("serviceNumberExact");
        String ticketType = StringUtils.isEmpty(searchFormData.get("ticketType")) ? null : searchFormData.get("ticketType");
        String serviceType = StringUtils.isEmpty(searchFormData.get("serviceType")) ? null : searchFormData.get("serviceType");
        boolean isReport = BooleanUtils.toBoolean(searchFormData.get("isReport"));
        boolean isApiFlag = BooleanUtils.toBoolean(searchFormData.get("isApiFlag"));

        List<String> owningRole = null;
        if (StringUtils.isEmpty(searchFormData.get("owningRole"))) {
            if (!isApiFlag) {
                owningRole = userRoleFacade.getCurrentUserUserRole().stream()
                        .filter(sdUserRoleData -> !StringUtils.equals(sdUserRoleData.getRoleId(), "SYS_ADMIN"))
                        .map(SdUserRoleData::getRoleId).collect(Collectors.toList());
            }
        } else {
            owningRole = List.of(searchFormData.get("owningRole"));
        }

        Page<SdTicketMasBean> pageBean;
        try {
            pageBean = ticketService.searchTicketList(
                    pageable, createDateFrom, createDateTo,
                    status, completeDateFrom, completeDateTo,
                    createBy, ticketMasId, custCode,
                    serviceNumber, serviceNumberExact, ticketType, serviceType, isReport, owningRole);
        } catch (AuthorityNotFoundException e) {
            return new PageData<>(e.getMessage());
        }

        List<SdTicketMasBean> beanList = pageBean.getContent();
        return new PageData<>(buildTicketDataList(beanList), pageBean.getPageable(), pageBean.getTotalElements());
    }

    @Override
    public PageData<SdTicketMasData> getMyTicket(Pageable pageable) {
        Page<SdTicketMasBean> pageBean;
        try {
            pageBean = ticketService.getMyTicket(pageable);
        } catch (AuthorityNotFoundException e) {
            return new PageData<>(e.getMessage());
        }

        List<SdTicketMasBean> beanList = pageBean.getContent();
        return new PageData<>(buildTicketDataList(beanList), pageBean.getPageable(), pageBean.getTotalElements());
    }

    private List<SdTicketMasData> buildTicketDataList(List<SdTicketMasBean> beanList) {
        // populate content
        List<SdTicketMasData> dataList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(beanList)) {
            for (SdTicketMasBean bean : beanList) {
                SdTicketMasData data = new SdTicketMasData();
                ticketMasDataPopulator.populate(bean, data);
                dataList.add(data);
            }
        }
        return dataList;
    }

    @Override
    public List<SdTicketRemarkData> getTicketRemarksByTicketId(Integer ticketMasId) {
        List<SdTicketRemarkData> dataList = new LinkedList<>();
        List<SdTicketRemarkBean> beanList = ticketService.getTicketRemarksByTicketId(ticketMasId);
        List<WfmJobProgressData> jobProgressDataList = wfmApiFacade.getJobProgessByTicketId(ticketMasId);
        List<WfmJobRemarksData> jobRemarkDataList = wfmApiFacade.getJobRemarkByTicketId(ticketMasId);


        if (CollectionUtils.isNotEmpty(beanList)) {
            for (SdTicketRemarkBean bean : beanList) {
                SdTicketRemarkData data = new SdTicketRemarkData();
                ticketRemarkDataPopulator.populate(bean, data);
                dataList.add(data);
            }
        }

        if (CollectionUtils.isNotEmpty(jobProgressDataList)) {
            for (WfmJobProgressData bean : jobProgressDataList) {
                SdTicketRemarkData data = new SdTicketRemarkData();
                data.setTicketMasId(ticketMasId);
                ticketRemarkDataPopulator.populateJobProgressData(bean, data);
                dataList.add(data);
            }
        }

        if (CollectionUtils.isNotEmpty(jobRemarkDataList)) {
            for (WfmJobRemarksData bean : jobRemarkDataList) {
                SdTicketRemarkData data = new SdTicketRemarkData();
                data.setTicketMasId(ticketMasId);
                ticketRemarkDataPopulator.populateJobRemarkData(bean, data);
                dataList.add(data);
            }
        }

        // sort
        return dataList.stream()
                .sorted(Comparator.comparing(SdTicketRemarkData::getCreatedate, Comparator.nullsLast(LocalDateTime::compareTo)).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<SdTicketServiceData> getServiceInfo(Integer ticketMasId) {
        List<SdTicketServiceBean> serviceInfoList = ticketService.getServiceInfo(ticketMasId);
        if (CollectionUtils.isNotEmpty(serviceInfoList)) {
            List<SdTicketServiceData> ticketServiceDataList = serviceInfoList.stream().map(bean -> {
                SdTicketServiceData data = new SdTicketServiceData();
                ticketServiceDataPopulator.populate(bean, data);
                return data;
            }).collect(Collectors.toList());

            for (SdTicketServiceData serviceData : ticketServiceDataList) {
                switch (serviceData.getServiceType()) {
                    case SdServiceTypeBean.SERVICE_TYPE.ENTERPRISE_CLOUD:
                    case SdServiceTypeBean.SERVICE_TYPE.ENTERPRISE_CLOUD_365:
                        serviceData.setCloudCtrl(true);
                        break;
                    case SdServiceTypeBean.SERVICE_TYPE.VOIP:
                    case SdServiceTypeBean.SERVICE_TYPE.FIX_NUMBER:
                        serviceData.setVoIpCtrl(true);
                        break;
                    case SdServiceTypeBean.SERVICE_TYPE.BROADBAND:
                        serviceData.setBnCtrl(true);
                        break;
                    case SdServiceTypeBean.SERVICE_TYPE.SMART_METER:
                        serviceData.setMeterCtrl(true);
                        break;
                    case SdServiceTypeBean.SERVICE_TYPE.GMB:
                        serviceData.setGmbCtrl(true);
                }
                serviceData.setServiceTypeDesc(serviceTypeFacade.getServiceTypeDescByServiceTypeCode(serviceData.getServiceType()));
            }
            return ticketServiceDataList;
        }
        return null;
    }

    @Override
    public String updateServiceInfo(List<SdRequestTicketServiceData> serviceList) {
        if (CollectionUtils.isEmpty(serviceList)) {
            return "update service info failed.";
        }

        try {
            List<SdTicketServiceBean> serviceInfoList = serviceList.stream().map(requestData -> {
                SdTicketServiceBean bean = new SdTicketServiceBean();
                bean.setTicketMasId(requestData.getTicketMasId());
                bean.setServiceTypeCode(requestData.getServiceType());
                bean.setServiceId(requestData.getServiceCode());
                bean.setFaults(requestData.getFaults());
                bean.setReportTime(requestData.getReportTime());
                return bean;
            }).collect(Collectors.toList());

            for (SdTicketServiceBean serviceInfo : serviceInfoList) {
                List<String> faults = serviceInfo.getFaults();
                String serviceTypeCode = serviceInfo.getServiceTypeCode();
                LocalDateTime reportTime = serviceInfo.getReportTime();
                if (CollectionUtils.isEmpty(faults) || StringUtils.isEmpty(faults.stream().findFirst().get())) {
                    return "Please select symptom.";
                } else if (serviceTypeCode.equals(SdServiceTypeBean.SERVICE_TYPE.SMART_METER)
                        || serviceTypeCode.equals(SdServiceTypeBean.SERVICE_TYPE.GMB)){
                    if (reportTime == null) {
                        return "Please input report time.";
                    } else if (reportTime.isAfter(LocalDateTime.now())) {
                        return "Input report time can not after than now.";
                    }
                }
                faults.forEach(symptom ->
                        ticketService.updateServiceSymptom(serviceInfo.getTicketMasId(), symptom, serviceInfo.getReportTime()));
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return "update service info failed.";
        }
        return null;
    }

    @Override
    public void createTicketRemarks(Integer ticketMasId, String remarks) {
        if (ticketMasId == null) {
            throw new InvalidInputException("Ticket Mas ID is empty.");
        } else if (StringUtils.isEmpty(remarks)) {
            throw new InvalidInputException("Remarks is empty.");
        } else if (remarks.getBytes(StandardCharsets.UTF_8).length > 500) {
            throw new InvalidInputException("Input remark is too long. (max: 500 characters / 166 chinese words)");
        }

        try {
            ticketService.createTicketCustRemarks(ticketMasId, remarks);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("Duplicate data already exists.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJobIdInService(Integer jobId, int ticketMasId) {
        String userId = userService.getCurrentUserUserId();
        ticketService.updateJobIdInService(jobId, ticketMasId, userId);
        ticketService.updateTicketType(ticketMasId, "JOB", userId);
    }

    @Override
    public SdAppointmentData getAppointmentData(Integer ticketMasId) {
        if (ticketMasId == null) {
            LOG.warn("Empty ticketMasId.");
            return null;
        }

        // call WFM API
        WfmAppointmentResData wfmAppointmentResData;
        try {
            wfmAppointmentResData = wfmApiFacade.getAppointmentInfo(ticketMasId);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            wfmAppointmentResData = null;
        }

        // API error
        if (wfmAppointmentResData == null) {
            LOG.error("WFM Error: No response for ticketMasId " + ticketMasId);
            return null;
        }

        // transform
        try {
            LocalDateTime appointmentStartDateTime = StringUtils.isEmpty(wfmAppointmentResData.getAppointmentStartDateTime()) ?
                    null : LocalDateTime.parse(wfmAppointmentResData.getAppointmentStartDateTime(), WfmAppointmentResData.DATE_TIME_FORMATTER);
            LocalDateTime appointmentEndDateTime = StringUtils.isEmpty(wfmAppointmentResData.getAppointmentEndDateTime()) ?
                    null : LocalDateTime.parse(wfmAppointmentResData.getAppointmentEndDateTime(), WfmAppointmentResData.DATE_TIME_FORMATTER);

            String appointmentStartStr = appointmentStartDateTime == null ? StringUtils.EMPTY :
                    appointmentStartDateTime.toLocalDate().toString() + StringUtils.SPACE + appointmentStartDateTime.toLocalTime().toString();
            String appointmentEndStr = appointmentEndDateTime == null ? StringUtils.EMPTY : "-" + appointmentEndDateTime.toLocalTime().toString();
            String appointmentDateStr = String.format("%s%s", appointmentStartStr, appointmentEndStr);

            SdAppointmentData appointmentData = new SdAppointmentData();
            appointmentData.setAppointmentDateStr(appointmentDateStr);
            return appointmentData;
        } catch (DateTimeParseException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<SdSymptomData> getSymptom(Integer ticketMasId) {
        List<SdSymptomBean> beanList = ticketService.getSymptomList(ticketMasId);

        if (CollectionUtils.isEmpty(beanList)) {
            return null;
        }

        return beanList.stream().map(bean -> {
            SdSymptomData data = new SdSymptomData();
            ticketServiceDataPopulator.populate(bean, data);
            return data;
        }).collect(Collectors.toList());
    }

    @Override
    public BesSubFaultData getFaultInfo(String subscriberId, Pageable pageable) {
        if (StringUtils.isEmpty(subscriberId)) {
            return BesSubFaultData.MISSING_PARAM;
        }
        BesSubFaultData besSubFaultData = new BesSubFaultData();
        try {
            // get ticket paged det list
            List<SdTicketServiceBean> sdTicketServiceBeanList = ticketService.findServiceBySubscriberId(subscriberId, pageable);
            if (CollectionUtils.isEmpty(sdTicketServiceBeanList)) {
                return BesSubFaultData.NOT_FOUND;
            }

            // get all ticket info list
            List<SdTicketData> sdTicketDataList = new ArrayList<>();
            for (SdTicketServiceBean sdTicketServiceBean : sdTicketServiceBeanList) {
                SdTicketData sdTicketData = getTicketInfo(sdTicketServiceBean.getTicketMasId());
                sdTicketDataList.add(sdTicketData);
            }

            // transform SdTicketData to BesFaultInfoData
            List<BesFaultInfoData> besFaultInfoDataList = new ArrayList<>();
            for (SdTicketData sdTicketData : sdTicketDataList) {
                BesFaultInfoData besFaultInfoData = new BesFaultInfoData();
                faultInfoDataPopulator.populate(sdTicketData, besFaultInfoData);
                besFaultInfoDataList.add(besFaultInfoData);
            }
            if (pageable == null) {
                besSubFaultData.setList(besFaultInfoDataList);
            } else {
                long count = ticketService.countServiceBySubscriberId(subscriberId);
                besSubFaultData.setPagedList(new PageData<>(besFaultInfoDataList, pageable, count));
            }
            return besSubFaultData;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return BesSubFaultData.FAIL;
        }
    }

    @Override
    public SdTicketData getTicketInfo(Integer ticketMasId) {
        SdTicketMasData ticketMasInfo = getTicketMas(ticketMasId);
        List<SdTicketContactData> contactInfo = getContactInfo(ticketMasId);
        List<SdTicketServiceData> serviceInfo = getServiceInfo(ticketMasId);
        List<SdTicketRemarkData> remarkInfo = getTicketRemarksByTicketId(ticketMasId);
        List<Map<String, Object>> closeInfo = getCloseInfo(ticketMasId);
        List<Parameter> parameterList = getParameterList(ticketMasId);

        SdTicketData ticketData = new SdTicketData();
        ticketData.setTicketMasInfo(ticketMasInfo);
        ticketData.setContactInfo(contactInfo);
        ticketData.setServiceInfo(serviceInfo);
        ticketData.setRemarkInfo(remarkInfo);
        ticketData.setCloseInfo(closeInfo);
        ticketData.setParameterList(parameterList);

        return ticketData;
    }

    private List<Parameter> getParameterList(Integer ticketMasId) {
        SdGmbTicketEavBean bean = ticketService.getGmbTicketOtherInfo(ticketMasId);

        if (bean == null) {
            return null;
        }

        List<Parameter> dataList = new LinkedList<>();
        dataList.add(Parameter.of("psl", bean.getPsl()));
        dataList.add(Parameter.of("routeNo", bean.getRouteNo()));
        return dataList;
    }

    private List<Map<String, Object>> getCloseInfo(Integer ticketMasId) {
        List<SdTicketServiceBean> ticketServiceBeanList = ticketService.getCloseInfo(ticketMasId);

        if (CollectionUtils.isEmpty(ticketServiceBeanList)) {
            return null;
        }

        List<Map<String, Object>> dataList = new LinkedList<>();
        ticketServiceBeanList.stream().findFirst().filter(sdTicketServiceBean -> sdTicketServiceBean.getWfmCompleteInfo() != null).ifPresent(sdTicketServiceBean -> {
            String wfmCompleteInfo = sdTicketServiceBean.getWfmCompleteInfo();
            Type type = new TypeToken<List<Map<String, Object>>>() {}.getType();
            List<Map<String, Object>> list = new Gson().fromJson(wfmCompleteInfo, type);
            dataList.addAll(list);
        });

        return dataList;
    }

    public SdTicketMasData getTicketMas(Integer ticketMasId) {
        SdTicketMasData sdticketMasData = new SdTicketMasData();
        Optional<SdTicketMasBean> sdTicketMasBean = ticketService.getTicket(ticketMasId);
        sdTicketMasBean.ifPresent(ticketMasBean -> ticketMasDataPopulator.populate(ticketMasBean, sdticketMasData));
        return sdticketMasData;
    }

    @Override
    public List<SdTicketMasData> getPendingTicketList(String serviceType, String serviceNo) {
        List<SdTicketMasBean> beanList = ticketService.getPendingTicketList(serviceType, serviceNo);
        return buildTicketDataList(beanList);
    }

    @Override
    public String closeTicketByApi(WfmTicketCloseData wfmTicketCloseData) {
        String systemId = userService.getCurrentUserUserId();
        Integer ticketMasId = wfmTicketCloseData.getTicketMasId();
        String reasonContent = wfmTicketCloseData.getReasonContent();
        String reasonType = wfmTicketCloseData.getReasonType();
        CharSequence arrivalTimeStr = wfmTicketCloseData.getArrivalTime();
        String userId = wfmTicketCloseData.getUsername();
        List<WfmCompleteInfo> wfmCompleteInfo = wfmTicketCloseData.getWfmCompleteInfo();
        LOG.info(String.format("Closing ticket by API. (ticketMasId: %d , systemId: %s)", ticketMasId, systemId));

        LocalDateTime arrivalTime = null;
        if (StringUtils.isEmpty(reasonContent)) {
            reasonContent = "Empty sub-clear code.";
        }

        if (StringUtils.isNotEmpty(arrivalTimeStr)){
            try {
                arrivalTime = LocalDateTime.parse(arrivalTimeStr, DATE_TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                return "Invalid arrivalTime format. " + DATE_TIME_FORMATTER;
            }
        }

        // close ticket in servicedesk
        try {
            ticketService.closeTicket(ticketMasId, null, reasonType, reasonContent, arrivalTime, systemId, userId, wfmCompleteInfo, false);
            LOG.info("Closed (by API) ticket in servicedesk. (ticketMasId: " + ticketMasId + ")");
        } catch (InvalidInputException e) {
            LOG.warn(e.getMessage());
            return e.getMessage();
        }

        // notify oss to close ticket
        smartMeterFacade.notifyCloseTicket(ticketMasId);
        return null;
    }

    @Override
    public String closeTicket(int ticketMasId, String closeCode, String reasonType, String reasonContent, String contactName, String contactNumber) {
        LOG.info(String.format("Closing ticket. (ticketMasId: %d)", ticketMasId));

        // close ticket in servicedesk
        try {
            ticketService.closeTicket(ticketMasId, closeCode, reasonType, reasonContent, null, contactName, contactNumber, null, true);
        } catch (InvalidInputException e) {
            LOG.warn(e.getMessage());
            return e.getMessage();
        }

        // notify oss to close ticket
        smartMeterFacade.notifyCloseTicket(ticketMasId);

        // notify wfm to close ticket
        boolean isClosedInWfm = wfmApiFacade.closeTicket(ticketMasId);
        if (!isClosedInWfm) {
            String wfmFail = "Cannot notify WFM to close ticket! (ticketMasId: " + ticketMasId + ")";
            LOG.warn(wfmFail);
            return wfmFail;
        }

        return null;
    }

    @Override
    public void isAllow(int ticketMasId, String action) {
        switch (action) {
            case SdTicketMasData.ACTION_TYPE.WORKING:
                ticketService.getTicket(ticketMasId).map(SdTicketMasBean::getStatus)
                        .filter(s -> s.equals(TicketStatusEnum.OPEN))
                        .orElseThrow(() -> new RuntimeException("Cannot update. This ticket has been passed to working parties."));
                break;
            case SdTicketMasData.ACTION_TYPE.COMPLETE:
                ticketService.getTicket(ticketMasId).map(SdTicketMasBean::getStatus)
                        .filter(s -> !s.equals(TicketStatusEnum.COMPLETE))
                        .orElseThrow(() -> new RuntimeException("Cannot update. This ticket has been completed."));
                break;
            default:
                ticketService.getTicket(ticketMasId).map(SdTicketMasBean::getStatus)
                        .filter(s -> (s == TicketStatusEnum.WORKING || s == TicketStatusEnum.COMPLETE)).ifPresent(s -> {
                    if (s.equals(TicketStatusEnum.COMPLETE)) {
                        throw new RuntimeException("Cannot update. This ticket has been completed.");
                    } else {
                        throw new RuntimeException("Cannot update. This ticket has been passed to working parties.");
                    }
                });
        }
    }

    @Override
    public boolean increaseCallInCount(Integer ticketMasId) {
        if (ticketMasId == null) {
            return false;
        }

        try {
            ticketService.increaseCallInCount(ticketMasId);
            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void createJob4Wfm(int ticketMasId, boolean notifyOss) throws InvalidInputException, ApiException {
        SdTicketData ticketInfo = getTicketInfo(ticketMasId);
        if(ticketInfo==null){
            throw new InvalidInputException("Ticket not found.");
        }

        // get exchange for smart meter
        SdTicketMasData ticketMasData = ticketInfo.getTicketMasInfo();
        if (ServiceSearchEnum.POLE_ID.getKey().equals(ticketMasData.getSearchKey())) {
            String serviceNumber = ticketMasData == null ? null : ticketMasData.getSearchValue();
            String exchangeId = getExchangeIdByPoleId(serviceNumber);
            if (StringUtils.isEmpty(exchangeId)) {
                throw new InvalidInputException("Exchange not found.");
            }
            ticketMasData.setExchangeId(exchangeId);
        }

        // check service
        Integer poleId = null;
        String plateId = null;
        for (SdTicketServiceData serviceData : ticketInfo.getServiceInfo()) {
            // check symptom
            if (CollectionUtils.isEmpty(serviceData.getFaultsList())) {
                throw new InvalidInputException("Please select a symptom.");
            }

            // check by service type
            if (SdServiceTypeBean.SERVICE_TYPE.UNKNOWN.equals(serviceData.getServiceType())) {
                throw new InvalidInputException("Unknown service type.");
            } else if (SdServiceTypeBean.SERVICE_TYPE.SMART_METER.equals(serviceData.getServiceType())){
                poleId = Integer.parseInt(serviceData.getServiceCode());
                if(serviceData.getReportTime()==null){
                    throw new InvalidInputException("Please input the report time.");
                }
            } else if (SdServiceTypeBean.SERVICE_TYPE.GMB.equals(serviceData.getServiceType())) {
                plateId = serviceData.getServiceCode();
                if(serviceData.getReportTime()==null){
                    throw new InvalidInputException("Please input the report time.");
                }
            }
        }

        // check contact
        if(poleId==null || plateId==null) {
            if (CollectionUtils.isEmpty(ticketInfo.getContactInfo())) {
                throw new InvalidInputException("Please input contact.");
            }
        }

        try {
            Integer jobId = wfmApiFacade.createJob(ticketInfo);
            updateJobIdInService(jobId, ticketMasId);
        } catch (JsonProcessingException e) {
            throw new ApiException(String.format("WFM Error: Cannot create job for ticket mas id %s.", ticketMasId));
        }

        // notify oss for hotline smart meter job ticket
        String createDate = ticketMasData.getCreateDate() == null ? null : ticketMasData.getCreateDate().format(DEFAULT_DATE_TIME_FORMAT);
        if (poleId != null && notifyOss){
            ossApiFacade.notifyTicketStatus(poleId, ticketMasId, createDate, OssTicketActionEnum.CREATE.getCode());
        } else if (plateId != null && notifyOss) {
            gmbApiFacade.notifyTicketStatus(plateId, ticketMasId, createDate, OssTicketActionEnum.CREATE.getCode());
        }
    }

    private String getExchangeIdByPoleId(String serviceNumber) {
        if (StringUtils.isEmpty(serviceNumber)) {
            return null;
        }

        OssSmartMeterData ossSmartMeterData = ossApiFacade.queryMeterInfo(serviceNumber.replaceAll(StringUtils.SPACE, StringUtils.EMPTY));
        if( ossSmartMeterData==null || StringUtils.isEmpty(ossSmartMeterData.getPoleId())){
            String warnMsg = "Meter profile not found in OSS. (poleId=" + serviceNumber + ")";
            LOG.warn(warnMsg);
            return null;
        }

        return ossSmartMeterData.getExchange();
    }

    @Override
    public List<BtuCodeDescData> getTicketStatusList() {
        List<TicketStatusEnum> ticketStatusEnumList = ticketService.getTicketStatusList();
        if (CollectionUtils.isEmpty(ticketStatusEnumList)) {
            return null;
        }

        List<BtuCodeDescData> codeDescDataList = new ArrayList<>();
        for (TicketStatusEnum ticketStatusEnum : ticketStatusEnumList) {
            BtuCodeDescData codeDescData = new BtuCodeDescData();
            codeDescData.setCode(ticketStatusEnum.getStatusCode());
            codeDescData.setCodeDesc(ticketStatusEnum.getStatusDesc());
            codeDescDataList.add(codeDescData);
        }
        return codeDescDataList;
    }

    @Override
    public List<BtuCodeDescData> getTicketTypeList() {
        List<TicketTypeEnum> ticketTypeEnumList = ticketService.getTicketTypeList();
        if (CollectionUtils.isEmpty(ticketTypeEnumList)) {
            return null;
        }

        List<BtuCodeDescData> codeDescDataList = new ArrayList<>();
        for (TicketTypeEnum ticketTypeEnum : ticketTypeEnumList) {
            BtuCodeDescData codeDescData = new BtuCodeDescData();
            codeDescData.setCode(ticketTypeEnum.getTypeCode());
            codeDescData.setCodeDesc(ticketTypeEnum.getTypeDesc());
            codeDescDataList.add(codeDescData);
        }
        return codeDescDataList;
    }

    @Override
    public SdTeamSummaryData getTeamSummary() {
        SdTeamSummaryData data = new SdTeamSummaryData();
        SdTeamSummaryBean summaryBean = ticketService.getTeamSummary();
        teamSummaryDataPopulator.populate(summaryBean, data);
        return data;
    }

    @Override
    public List<SdCloseCodeData> getCloseCode(String serviceType) {
        List<SdCloseCodeBean> beanList = ticketService.getCloseCodeList(serviceType);

        if (CollectionUtils.isEmpty(beanList)) {
            return null;
        }

        return beanList.stream().map(bean -> {
            SdCloseCodeData data = new SdCloseCodeData();
            sdCloseCodeDataPopulator.populate(bean, data);
            return data;
        }).collect(Collectors.toList());
    }

    @Override
    public String getSymptomForApi(String serviceType, List<String> workingPartyList) {
        if (workingPartyList.size() == 1) {
            String workingParty = workingPartyList.stream().collect(Collectors.joining(","));
            List<SdSymptomWorkingPartyMappingBean> beanList = ticketService.getSymptomByServiceType(serviceType, workingParty);
            return CollectionUtils.isEmpty(beanList) ? null : beanList.get(0).getSymptomCode();
        } else if (workingPartyList.size() > 1) {
            List<SdSymptomWorkingPartyMappingBean> beanList = ticketService.getSymptomByServiceType(serviceType, null);
            if (CollectionUtils.isEmpty(beanList)) {
                return null;
            }
            for (SdSymptomWorkingPartyMappingBean bean : beanList) {
                List<String> dbWorkingPartyList = Arrays.asList(bean.getWorkingParty().split(","));
                if (dbWorkingPartyList.size() > 1 && dbWorkingPartyList.containsAll(workingPartyList)) {
                    return bean.getSymptomCode();
                }
            }
        }

        return null;
    }

    public PageData<SdTicketMasData> searchBchspList(Pageable pageable, Map<String, String> searchFormData) {
        LocalDate createDateFrom = StringUtils.isEmpty(searchFormData.get("createDateFrom")) ? null : LocalDate.parse(searchFormData.get("createDateFrom"));
        LocalDate createDateTo = StringUtils.isEmpty(searchFormData.get("createDateTo")) ? null : LocalDate.parse(searchFormData.get("createDateTo"));

        String status = StringUtils.isEmpty(searchFormData.get("status")) ? null : searchFormData.get("status");
        LocalDate completeDateFrom = StringUtils.isEmpty(searchFormData.get("completeDateFrom")) ? null : LocalDate.parse(searchFormData.get("completeDateFrom"));
        LocalDate completeDateTo = StringUtils.isEmpty(searchFormData.get("completeDateTo")) ? null : LocalDate.parse(searchFormData.get("completeDateTo"));

        String createBy = StringUtils.isEmpty(searchFormData.get("createBy")) ? null : searchFormData.get("createBy");
        String ticketMasId = StringUtils.isEmpty(searchFormData.get("ticketMasId")) ? null : searchFormData.get("ticketMasId");
        String custCode = StringUtils.isEmpty(searchFormData.get("custCode")) ? null : searchFormData.get("custCode");

        String serviceNumber = StringUtils.isEmpty(searchFormData.get("serviceNumber")) ? null : searchFormData.get("serviceNumber");
        String ticketType = StringUtils.isEmpty(searchFormData.get("ticketType")) ? null : searchFormData.get("ticketType");
        String serviceType = StringUtils.isEmpty(searchFormData.get("serviceType")) ? null : searchFormData.get("serviceType");
        String owningRole = StringUtils.isEmpty(searchFormData.get("owningRole")) ? null : searchFormData.get("owningRole");
        String workGroup = StringUtils.isEmpty(searchFormData.get("workGroup")) ? null : searchFormData.get("workGroup");

        Page<SdTicketMasBean> pageBean;
        try {
            pageBean = ticketService.searchBchspList(
                    pageable, createDateFrom, createDateTo,
                    status, completeDateFrom, completeDateTo,
                    createBy, ticketMasId, custCode, serviceNumber,
                    ticketType, serviceType, owningRole, workGroup);
        } catch (AuthorityNotFoundException e) {
            return new PageData<>(e.getMessage());
        }

        List<SdTicketMasBean> beanList = pageBean.getContent();
        return new PageData<>(buildTicketDataList(beanList), pageBean.getPageable(), pageBean.getTotalElements());
    }

    @Override
    public String getJobId(String ticketMasId) {
        return ticketService.getJobId(ticketMasId);
    }

    @Override
    public List<String> getWorkGroupList() {
        return ticketService.getWorkGroupList();
    }

    @Override
    public WfmMakeApptData getMakeApptDataByTicketDetId(Integer ticketDetId) {
        SdMakeApptBean bean = ticketService.getTicketServiceByDetId(ticketDetId);

        if (bean == null) {
            return null;
        }

        WfmMakeApptData data = new WfmMakeApptData();
        data.setBsn(bean.getBsn());
        data.setServiceType(bean.getServiceType());
        data.setTicketMasId(bean.getTicketMasId());
        data.setTicketDetId(bean.getTicketDetId());
        data.setSymptomCode(bean.getSymptomCode());

        return data;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createTicket4hktCloud(HktCloudCaseData cloudCaseData) {
        int ticketId = Integer.parseInt(cloudCaseData.getRequestId());
        String contactEmail = "";
        String contactNumber = "";
        StringBuilder remark = new StringBuilder();
        for (Attribute attr : cloudCaseData.getAttributes()) {
            if (StringUtils.equals("Contact Email", attr.getAttrName())) {
                contactEmail = attr.getAttrValue();
            } else if (StringUtils.equals("Contact Phone", attr.getAttrName())) {
                contactNumber = attr.getAttrValue();
            } else {
                remark.append(attr.getAttrName()).append(":").append(StringUtils.isEmpty(attr.getAttrValue())? "NULL":attr.getAttrValue()).append(",");
            }
        }
        ticketService.createHktCloudTicket(ticketId,cloudCaseData.getTenantId(),cloudCaseData.getCreatedBy());
        ticketService.insertTicketContactInfo(ticketId,"CUST",cloudCaseData.getCreatedBy(),contactNumber,contactEmail,"");
        ticketService.createTicketCustRemarks(ticketId,remark.toString());
        for (Attachment attachment : cloudCaseData.getAttachments()) {
            ticketService.insertUploadFile(ticketId, attachment.getFileName(), attachment.getContent());
        }
        return "Creation success.";
    }

    @Override
    public String getNewTicketId() {
        return ticketService.getNewTicketId();
    }

    @Override
    public List<HktCloudViewData> getHktCloudTicket(String tenantId, String username) {
        return ticketService.getHktCloudTicket(tenantId,username).stream().map(bean -> {
            HktCloudViewData data = new HktCloudViewData();
            cloudViewDataPopulator.populate(bean,data);
            return data;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SdTicketUploadFileData> getUploadFiles(int ticketMasId) {
        return ticketService.getUploadFiles(ticketMasId).stream().map(bean -> {
            SdTicketUploadFileData data = new SdTicketUploadFileData();
            ticketUploadFileDataPopulator.populate(bean,data);
            return data;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertUploadFile(Integer ticketMasId, List<Attachment> attachments) {
        try {
            ticketService.removeUploadFileByTicketMasId(ticketMasId);
            for (Attachment attachment : attachments) {
                ticketService.insertUploadFile(ticketMasId, attachment.getFileName(), attachment.getContent());
            }
        } catch (Exception e) {
            String errorMsg = "file upload faild.";
            LOG.error(errorMsg);
            return errorMsg;
        }
        return null;
    }

    @Override
    public void insertExtraInfo(Integer ticketMasId, List<Attribute> attributes) {
        try {
            ticketService.deleteEntityVarchar(ticketMasId);
            attributes.forEach(entity -> {
                if (StringUtils.isNotEmpty(entity.getAttrName()) && StringUtils.isNotEmpty(entity.getAttrValue())) {
                    ticketService.insertExtraInfo(ticketMasId, entity.getAttrName(), entity.getAttrValue());
                }
            });
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    @Override
    public List<SdTicketExportData> searchTicketListForExport(Map<String, String> searchFormData) {
        LocalDate createDateFrom = StringUtils.isEmpty(searchFormData.get("createDateFrom")) ? null : LocalDate.parse(searchFormData.get("createDateFrom"));
        LocalDate createDateTo = StringUtils.isEmpty(searchFormData.get("createDateTo")) ? null : LocalDate.parse(searchFormData.get("createDateTo"));

        String status = StringUtils.isEmpty(searchFormData.get("status")) ? null : searchFormData.get("status");
        LocalDate completeDateFrom = StringUtils.isEmpty(searchFormData.get("completeDateFrom")) ? null : LocalDate.parse(searchFormData.get("completeDateFrom"));
        LocalDate completeDateTo = StringUtils.isEmpty(searchFormData.get("completeDateTo")) ? null : LocalDate.parse(searchFormData.get("completeDateTo"));

        String createBy = StringUtils.isEmpty(searchFormData.get("createBy")) ? null : searchFormData.get("createBy");
        String ticketMasId = StringUtils.isEmpty(searchFormData.get("ticketMasId")) ? null : searchFormData.get("ticketMasId");
        String custCode = StringUtils.isEmpty(searchFormData.get("custCode")) ? null : searchFormData.get("custCode");

        String serviceNumber = StringUtils.isEmpty(searchFormData.get("serviceNumber")) ? null : searchFormData.get("serviceNumber");
        String ticketType = StringUtils.isEmpty(searchFormData.get("ticketType")) ? null : searchFormData.get("ticketType");
        String serviceType = StringUtils.isEmpty(searchFormData.get("serviceType")) ? null : searchFormData.get("serviceType");

        List<String> owningRole = null;
        if (StringUtils.isEmpty(searchFormData.get("owningRole"))) {
            owningRole = userRoleFacade.getCurrentUserUserRole().stream()
                    .filter(sdUserRoleData -> !StringUtils.equals(sdUserRoleData.getRoleId(), "SYS_ADMIN"))
                    .map(SdUserRoleData::getRoleId).collect(Collectors.toList());
        } else {
            owningRole = List.of(searchFormData.get("owningRole"));
        }

        List<SdTicketExportBean> beanList = ticketService.searchTicketListForExport(createDateFrom, createDateTo,
                status, completeDateFrom, completeDateTo, createBy, ticketMasId, custCode, serviceNumber, ticketType, serviceType, owningRole);

        List<SdTicketExportData> dataList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(beanList)) {
            for (SdTicketExportBean bean : beanList) {
                SdTicketExportData data = new SdTicketExportData();
                ticketMasDataPopulator.populate(bean, data);
                dataList.add(data);
            }
        }

        return dataList;
    }

    @Override
    public void fillSheet(HSSFSheet sheet, List<SdTicketExportData> accessRequestDataList) {
        List<List<String>> result = new ArrayList<>();
        if (sheet == null) {
            LOG.error("Null sheet. Cannot create cvs.");
            return;
        }

        if (CollectionUtils.isEmpty(accessRequestDataList)) {
            LOG.error("The list from fillITSAOSDailyReport is empty.");
        } else {
            for (SdTicketExportData data : accessRequestDataList) {
                List<String> row = buildRow(data);
                result.add(row);
            }
        }

        POIExcelWriter.appendRetrievedData(sheet, new String[]
                {"Ticket Id", "Ticket Type", "Status", "Complete Date", "Call In Count", "Service Number", "Owning Role",
                        "Create Date", "Create By", "Modify Date", "Modify By", "Symptom Description", "Subs ID", "Job ID",
                        "Report Time", "SD CloseCode Description", "WFM ClearCode", "WFM Sub ClearCode"}, result);
    }

    @Override
    public String getFileName() {
        final LocalDateTime NOW = LocalDateTime.now();
        String formatLocalDateTime = NOW.format(FILE_TIMESTAMP_FORMATTER);
        String fileName = "Ticket_" + formatLocalDateTime + XLS_SUFFIX;
        return fileName;
    }

    private List<String> buildRow(SdTicketExportData data) {
        if (data == null) {
            return new ArrayList<>();
        }

        // extract from bean
        String ticketMasId = String.valueOf(data.getTicketMasId());
        String ticketType = data.getTicketType() == null ? StringUtils.EMPTY : data.getTicketType();
        String status = data.getStatus() == null ? StringUtils.EMPTY : data.getStatus();
        String completeDate = data.getCompleteDate() == null ? StringUtils.EMPTY : data.getCompleteDate().format(DEFAULT_DATE_TIME_FORMAT);
        String callInCount = String.valueOf(data.getCallInCount());
        String serviceNumber = data.getServiceNumber() == null ? StringUtils.EMPTY : data.getServiceNumber();
        String owningRole = data.getOwningRole() == null ? StringUtils.EMPTY : data.getOwningRole();
        String createDate = data.getCreateDate() == null ? StringUtils.EMPTY : data.getCreateDate().format(DEFAULT_DATE_TIME_FORMAT);
        String createBy = data.getCreateBy() == null ? StringUtils.EMPTY : data.getCreateBy();
        String modifyDate = data.getModifyDate() == null ? StringUtils.EMPTY : data.getModifyDate().format(DEFAULT_DATE_TIME_FORMAT);
        String modifyBy = data.getModifyBy() == null ? StringUtils.EMPTY : data.getModifyBy();
        String symptomDescription = data.getSymptomDescription() == null ? StringUtils.EMPTY : data.getSymptomDescription();
        String subsId = data.getSubsId() == null ? StringUtils.EMPTY : data.getSubsId();
        String jobId = data.getJobId() == null ? StringUtils.EMPTY : data.getJobId();
        String reportTime = data.getReportTime() == null ? StringUtils.EMPTY : data.getReportTime().format(DEFAULT_DATE_TIME_FORMAT);
        String sdCloseCodeDescription = data.getSdCloseCodeDescription() == null ? StringUtils.EMPTY : data.getSdCloseCodeDescription();
        String wfmClearCode = data.getWfmClearCode() == null ? StringUtils.EMPTY : data.getWfmClearCode();
        String wfmSubClearCode = data.getWfmSubClearCode() == null ? StringUtils.EMPTY : data.getWfmSubClearCode();

        // add to row
        List<String> row = new ArrayList<>();
        row.add(ticketMasId);
        row.add(ticketType);
        row.add(status);
        row.add(completeDate);
        row.add(callInCount);
        row.add(serviceNumber);
        row.add(owningRole);
        row.add(createDate);
        row.add(createBy);
        row.add(modifyDate);
        row.add(modifyBy);
        row.add(symptomDescription);
        row.add(subsId);
        row.add(jobId);
        row.add(reportTime);
        row.add(sdCloseCodeDescription);
        row.add(wfmClearCode);
        row.add(wfmSubClearCode);
        return row;
    }

    @Override
    public List<SdOutstandingFaultData> getOutstandingFault() {
        return ticketService.getOutstandingFault().stream().map(bean -> {
            SdOutstandingFaultData data = new SdOutstandingFaultData();
            outstandingFaultDataPopulator.populate(bean, data);
            return data;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SdTicketTimePeriodSummaryData> getTicketTimePeriodSummary() {
        return ticketService.getTicketTimePeriodSummary().stream().map(bean -> {
            SdTicketTimePeriodSummaryData data = new SdTicketTimePeriodSummaryData();
            ticketTimePeriodSummaryDataPopulator.populate(bean, data);
            return data;
        }).collect(Collectors.toList());
    }

    @Override
    public String getAvgFaultCleaningTime() {
        return ticketService.getAvgFaultCleaningTime();
    }

    @Override
    @Transactional
    public void createJob4Wfm(Integer ticketMasId, List<SdTicketContactData> contactList, List<SdRequestTicketServiceData> serviceList, String remarks) throws InvalidInputException, ApiException, RuntimeException {
        // check contact
        for (SdTicketContactData data : contactList) {
            if (StringUtils.isEmpty(data.getContactName())) {
                throw new InvalidInputException("In contact type : " + data.getContactType() + ", please input contact name.");
            } else if (StringUtils.isEmpty(data.getContactNumber()) && StringUtils.isEmpty(data.getContactMobile()) && StringUtils.isEmpty(data.getContactEmail())) {
                throw new InvalidInputException("In Contact Name : " + data.getContactName() + ", please input Contact No. or Contact Mobile or Contact Email, at least one is not empty.");
            }
        }

        // check service
        SdRequestTicketServiceData serviceInfo = serviceList.stream().findFirst().get();
        List<String> faults = serviceInfo.getFaults();
        String serviceTypeCode = serviceInfo.getServiceType();
        LocalDateTime reportTime = serviceInfo.getReportTime();
        if (CollectionUtils.isEmpty(faults) || StringUtils.isEmpty(faults.stream().findFirst().get())) {
            throw new InvalidInputException("Please select symptom.");
        } else if (serviceTypeCode.equals(SdServiceTypeBean.SERVICE_TYPE.SMART_METER)
                || serviceTypeCode.equals(SdServiceTypeBean.SERVICE_TYPE.GMB)){
            if (reportTime == null) {
                throw new InvalidInputException("Please input report time.");
            } else if (reportTime.isAfter(LocalDateTime.now())) {
                throw new InvalidInputException("Input report time can not after than now.");
            }
        }

        // check remarks
        if (StringUtils.isNotEmpty(remarks)) {
            if (remarks.getBytes(StandardCharsets.UTF_8).length > 500) {
                throw new InvalidInputException("Input remark is too long. (max: 500 characters / 166 chinese words)");
            }
        }

        // check ticket
        SdTicketData ticketInfo = getTicketInfo(ticketMasId);
        if(ticketInfo==null){
            throw new InvalidInputException("Ticket not found.");
        }

        // get exchange for smart meter
        SdTicketMasData ticketMasData = ticketInfo.getTicketMasInfo();
        if (ServiceSearchEnum.POLE_ID.getKey().equals(ticketMasData.getSearchKey())) {
            String serviceNumber = ticketMasData == null ? null : ticketMasData.getSearchValue();
            String exchangeId = getExchangeIdByPoleId(serviceNumber);
            if (StringUtils.isEmpty(exchangeId)) {
                throw new InvalidInputException("Exchange not found.");
            }
            ticketMasData.setExchangeId(exchangeId);
        }

        Integer poleId = null;
        String plateId = null;
        for (SdTicketServiceData serviceData : ticketInfo.getServiceInfo()) {
            // check by service type
            if (SdServiceTypeBean.SERVICE_TYPE.UNKNOWN.equals(serviceData.getServiceType())) {
                throw new InvalidInputException("Unknown service type.");
            } else if (SdServiceTypeBean.SERVICE_TYPE.SMART_METER.equals(serviceData.getServiceType())){
                poleId = Integer.parseInt(serviceData.getServiceCode());
            } else if (SdServiceTypeBean.SERVICE_TYPE.GMB.equals(serviceData.getServiceType())) {
                plateId = serviceData.getServiceCode();
            }
        }

        // update contact
        try {
            ticketService.removeContactInfoByTicketMasId(contactList.get(0).getTicketMasId());
            contactList.forEach(data ->
                    ticketService.insertTicketContactInfo(data.getTicketMasId(), data.getContactType(),
                            data.getContactName(), data.getContactNumber(), data.getContactEmail(), data.getContactMobile()));
        } catch (Exception e) {
            throw new RuntimeException("Update contact info failed.");
        }

        // update service
        try {
            faults.forEach(symptom ->
                    ticketService.updateServiceSymptom(serviceInfo.getTicketMasId(), symptom, serviceInfo.getReportTime()));
        } catch (Exception e) {
            throw new RuntimeException("Update service info failed.");
        }

        // create remarks
        try {
            if (StringUtils.isNotEmpty(remarks)) {
                ticketService.createTicketCustRemarks(ticketMasId, remarks);
            }
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("Duplicate remarks already exists.");
        }

        // create wfm job
        try {
            Integer jobId = wfmApiFacade.createJob(ticketInfo);
            updateJobIdInService(jobId, ticketMasId);
        } catch (JsonProcessingException e) {
            throw new ApiException(String.format("WFM Error: Cannot create job for ticket mas id %s.", ticketMasId));
        }

        // notify oss for hotline smart meter job ticket
        String createDate = ticketMasData.getCreateDate() == null ? null : ticketMasData.getCreateDate().format(DEFAULT_DATE_TIME_FORMAT);
        if (poleId != null){
            ossApiFacade.notifyTicketStatus(poleId, ticketMasId, createDate, OssTicketActionEnum.CREATE.getCode());
        } else if (plateId != null) {
            gmbApiFacade.notifyTicketStatus(plateId, ticketMasId, createDate, OssTicketActionEnum.CREATE.getCode());
        }
    }
}
