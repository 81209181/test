package com.hkt.btu.sd.facade.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.core.exception.ApiException;
import com.hkt.btu.sd.core.exception.AuthorityNotFoundException;
import com.hkt.btu.sd.core.service.SdTicketService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.*;
import com.hkt.btu.sd.core.service.constant.TicketStatusEnum;
import com.hkt.btu.sd.core.service.constant.TicketTypeEnum;
import com.hkt.btu.sd.facade.SdAuditTrailFacade;
import com.hkt.btu.sd.facade.SdServiceTypeFacade;
import com.hkt.btu.sd.facade.SdTicketFacade;
import com.hkt.btu.sd.facade.WfmApiFacade;
import com.hkt.btu.sd.facade.constant.ServiceSearchEnum;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.data.wfm.WfmAppointmentResData;
import com.hkt.btu.sd.facade.data.wfm.WfmJobProgressData;
import com.hkt.btu.sd.facade.data.wfm.WfmJobRemarksData;
import com.hkt.btu.sd.facade.data.wfm.WfmMakeApptData;
import com.hkt.btu.sd.facade.populator.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class SdTicketFacadeImpl implements SdTicketFacade {

    private static final Logger LOG = LogManager.getLogger(SdTicketFacadeImpl.class);

    @Resource(name = "ticketService")
    SdTicketService ticketService;
    @Resource(name = "auditTrailFacade")
    SdAuditTrailFacade auditTrailFacade;
    @Resource(name = "userService")
    SdUserService userService;
    @Resource(name = "wfmApiFacade")
    WfmApiFacade wfmApiFacade;
    @Resource(name = "serviceTypeFacade")
    SdServiceTypeFacade serviceTypeFacade;

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

    @Override
    public int createQueryTicket(QueryTicketRequestData queryTicketRequestData) {
        if (!ServiceSearchEnum.TENANT_ID.getKey().equalsIgnoreCase(queryTicketRequestData.getSearchKey())) {
            if (StringUtils.isBlank(queryTicketRequestData.getCustCode())) {
                throw new InvalidInputException("Customer Code is Empty.");
            }
        }
        return ticketService.createQueryTicket(queryTicketRequestData.getCustCode(),
                queryTicketRequestData.getServiceNo(),
                queryTicketRequestData.getServiceType(),
                queryTicketRequestData.getSubsId(),
                queryTicketRequestData.getSearchKey(),
                queryTicketRequestData.getSearchValue());
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
    @Transactional(rollbackFor = Exception.class) // todo: Transactional best be in service layer
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
            contactList.forEach(data -> ticketService.insertTicketContactInfo(data.getTicketMasId(), data.getContactTypeValue(), data.getContactName(), data.getContactNumber(), data.getContactEmail(), data.getContactMobile()));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return "Update failed.";
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
        Page<SdTicketMasBean> pageBean;
        try {
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

            boolean isReport = BooleanUtils.toBoolean(searchFormData.get("isReport"));
            String owningRole = StringUtils.isEmpty(searchFormData.get("owningRole")) ? null : searchFormData.get("owningRole");

            pageBean = ticketService.searchTicketList(pageable, createDateFrom, createDateTo, status, completeDateFrom, completeDateTo, createBy, ticketMasId, custCode, serviceNumber, ticketType, serviceType,isReport,owningRole);
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
        List<SdTicketRemarkBean> beanList = ticketService.getTicketRemarksByTicketId(ticketMasId);
        List<WfmJobProgressData> jobProgressDataList = wfmApiFacade.getJobProgessByTicketId(ticketMasId);
        List<WfmJobRemarksData> jobRemarkDataList = wfmApiFacade.getJobRemarkByTicketId(ticketMasId);

        if (CollectionUtils.isEmpty(beanList) && CollectionUtils.isEmpty(jobProgressDataList) && CollectionUtils.isEmpty(jobRemarkDataList)) {
            return null;
        }

        List<SdTicketRemarkData> dataList = new LinkedList<>();
        for (SdTicketRemarkBean bean : beanList) {
            SdTicketRemarkData data = new SdTicketRemarkData();
            ticketRemarkDataPopulator.populate(bean, data);
            dataList.add(data);
        }

        for (WfmJobProgressData bean : jobProgressDataList) {
            SdTicketRemarkData data = new SdTicketRemarkData();
            ticketRemarkDataPopulator.populateJobProgressData(bean, data);
            dataList.add(data);
        }

        for (WfmJobRemarksData bean : jobRemarkDataList) {
            SdTicketRemarkData data = new SdTicketRemarkData();
            ticketRemarkDataPopulator.populateJobRemarkData(bean, data);
            dataList.add(data);
        }

        return dataList;
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
                        serviceData.setVoIpCtrl(true);
                        break;
                    case SdServiceTypeBean.SERVICE_TYPE.BROADBAND:
                        serviceData.setBnCtrl(true);
                        break;
                }
                serviceData.setServiceTypeDesc(serviceTypeFacade.getServiceTypeDescByServiceTypeCode(serviceData.getServiceType()));
            }
            return ticketServiceDataList;
        }
        return null;
    }

    @Override
    public String updateServiceInfo(List<RequestTicketServiceData> serviceList) {
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
                return bean;
            }).collect(Collectors.toList());

            for (SdTicketServiceBean serviceInfo : serviceInfoList) {
                List<String> faults = serviceInfo.getFaults();
                if (CollectionUtils.isEmpty(faults)) {
                    return "Please select symptom.";
                }
                faults.forEach(symptom ->
                        ticketService.updateServiceSymptom(serviceInfo.getTicketMasId(), symptom));
            }
        } catch (Exception e) {
            LOG.error(e);
            return "update service info failed.";
        }
        return null;
    }

    @Override
    public String createTicketRemarks(Integer ticketMasId, String remarks) {
        if (ticketMasId == null) {
            return "Ticket Mas ID is empty.";
        } else if (StringUtils.isEmpty(remarks)) {
            return "Remarks is empty.";
        }

        try {
            ticketService.createTicketCustRemarks(ticketMasId, remarks);
        } catch (DuplicateKeyException e) {
            return "Duplicate data already exists.";
        }

        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJobIdInService(Integer jobId, int ticketMasId) {
        String userId = userService.getCurrentUserUserId();
        ticketService.updateJobIdInService(jobId, ticketMasId, userId);
        ticketService.updateTicketType(ticketMasId, "JOB", userId);
    }

    @Override
    public SdTicketServiceData getService(Integer ticketId) {
        return ticketService.getService(ticketId).map(sdTicketServiceBean -> {
            SdTicketServiceData data = new SdTicketServiceData();
            ticketServiceDataPopulator.populate(sdTicketServiceBean, data);
            return data;
        }).orElse(null);
    }

    @Override
    public AppointmentData getAppointmentData(Integer ticketMasId) {
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

            AppointmentData appointmentData = new AppointmentData();
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
            // get ticket det list
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
            if (pageable==null) {
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

        SdTicketData ticketData = new SdTicketData();
        ticketData.setTicketMasInfo(ticketMasInfo);
        ticketData.setContactInfo(contactInfo);
        ticketData.setServiceInfo(serviceInfo);
        ticketData.setRemarkInfo(remarkInfo);

        return ticketData;
    }

    private SdTicketMasData getTicketMas(Integer ticketMasId) {
        SdTicketMasData sdticketMasData = new SdTicketMasData();
        Optional<SdTicketMasBean> sdTicketMasBean = ticketService.getTicket(ticketMasId);
        sdTicketMasBean.ifPresent(ticketMasBean -> ticketMasDataPopulator.populate(ticketMasBean, sdticketMasData));
        return sdticketMasData;
    }

    @Override
    public List<SdTicketMasData> getPendingTicketList(String serviceNo) {
        List<SdTicketMasBean> beanList = ticketService.getPendingTicketList(serviceNo);
        return buildTicketDataList(beanList);
    }

    @Override
    public String closeTicketByApi(int ticketMasId, String reasonType, String reasonContent, String userId) {
        String systemId = userService.getCurrentUserUserId();
        if (StringUtils.isNotEmpty(userId)) {
            userId = systemId + " - " + userId;
        }
        return closeTicket(ticketMasId, reasonType, reasonContent, userId, systemId, "");
    }

    @Override
    public String closeTicket(int ticketMasId, String reasonType, String reasonContent, String contactName, String contactNumber) {
        String currentUserId = userService.getCurrentUserUserId();
        return closeTicket(ticketMasId, reasonType, reasonContent, currentUserId, contactName, contactNumber);
    }

    private String closeTicket(int ticketMasId, String reasonType, String reasonContent, String closeby, String contactName, String contactNumber) {
        // close ticket in servicedesk
        try {
            ticketService.closeTicket(ticketMasId, reasonType, reasonContent, closeby, contactName, contactNumber);
            LOG.info("Closed ticket in servicedesk. (ticketMasId: " + ticketMasId + ")");
        } catch (InvalidInputException e) {
            LOG.warn(e.getMessage());
            return e.getMessage();
        }

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
    public void createJob4Wfm(int ticketMasId) throws InvalidInputException, ApiException {
        try {
            SdTicketData ticketInfo = getTicketInfo(ticketMasId);
            if (ticketInfo != null) {
                for (SdTicketServiceData serviceData : ticketInfo.getServiceInfo()) {
                    if (CollectionUtils.isEmpty(serviceData.getFaultsList())) {
                        throw new InvalidInputException("Please select a symptom.");
                    }
                    if (SdServiceTypeBean.SERVICE_TYPE.UNKNOWN.equals(serviceData.getServiceType())) {
                        throw new InvalidInputException("Unknown service type.");
                    }
                }
                if (CollectionUtils.isEmpty(ticketInfo.getContactInfo())) {
                    throw new InvalidInputException("Please input contact.");
                }
            } else {
                throw new InvalidInputException("Ticket not found.");
            }
            updateJobIdInService(wfmApiFacade.createJob(ticketInfo), ticketMasId);
        } catch (JsonProcessingException e) {
            throw new ApiException(String.format("WFM Error: Cannot create job for ticket mas id %s.", ticketMasId));
        }
    }

    @Override
    public List<CodeDescData> getTicketStatusList() {
        List<TicketStatusEnum> ticketStatusEnumList = ticketService.getTicketStatusList();
        if (CollectionUtils.isEmpty(ticketStatusEnumList)) {
            return null;
        }

        List<CodeDescData> codeDescDataList = new ArrayList<>();
        for (TicketStatusEnum ticketStatusEnum : ticketStatusEnumList) {
            CodeDescData codeDescData = new CodeDescData();
            codeDescData.setCode(ticketStatusEnum.getStatusCode());
            codeDescData.setCodeDesc(ticketStatusEnum.getStatusDesc());
            codeDescDataList.add(codeDescData);
        }
        return codeDescDataList;
    }

    @Override
    public List<CodeDescData> getTicketTypeList() {
        List<TicketTypeEnum> ticketTypeEnumList = ticketService.getTicketTypeList();
        if (CollectionUtils.isEmpty(ticketTypeEnumList)) {
            return null;
        }

        List<CodeDescData> codeDescDataList = new ArrayList<>();
        for (TicketTypeEnum ticketTypeEnum : ticketTypeEnumList) {
            CodeDescData codeDescData = new CodeDescData();
            codeDescData.setCode(ticketTypeEnum.getTypeCode());
            codeDescData.setCodeDesc(ticketTypeEnum.getTypeDesc());
            codeDescDataList.add(codeDescData);
        }
        return codeDescDataList;
    }

    @Override
    public TeamSummaryData getTeamSummary() {
        TeamSummaryData data = new TeamSummaryData();
        TeamSummaryBean summaryBean = ticketService.getTeamSummary();
        teamSummaryDataPopulator.populate(summaryBean,data);
        return data;
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
}
