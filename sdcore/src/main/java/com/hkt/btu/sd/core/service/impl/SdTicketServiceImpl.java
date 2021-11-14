package com.hkt.btu.sd.core.service.impl;

import com.google.gson.Gson;
import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.BtuSensitiveDataService;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.sd.core.dao.entity.*;
import com.hkt.btu.sd.core.dao.mapper.*;
import com.hkt.btu.sd.core.exception.InsufficientAuthorityException;
import com.hkt.btu.sd.core.service.SdTicketService;
import com.hkt.btu.sd.core.service.SdUserRoleService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.*;
import com.hkt.btu.sd.core.service.constant.TicketStatusEnum;
import com.hkt.btu.sd.core.service.constant.TicketTypeEnum;
import com.hkt.btu.sd.core.service.populator.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SdTicketServiceImpl implements SdTicketService {
    private static final Logger LOG = LogManager.getLogger(SdTicketServiceImpl.class);

    @Resource
    SdTicketMasMapper ticketMasMapper;
    @Resource
    SdTicketContactMapper ticketContactMapper;
    @Resource
    SdTicketServiceMapper ticketServiceMapper;
    @Resource
    SdTicketRemarkMapper ticketRemarkMapper;
    @Resource
    SdTicketFileUploadMapper ticketFileUploadMapper;
    @Resource
    private SdSymptomMapper symptomMapper;
    @Resource
    SdCloseCodeMapper sdCloseCodeMapper;
    @Resource
    SdTicketOtherMapper ticketOtherMapper;

    @Resource(name = "userService")
    SdUserService userService;
    @Resource(name = "sensitiveDataService")
    BtuSensitiveDataService sensitiveDataService;
    @Resource(name = "userRoleService")
    SdUserRoleService userRoleService;

    @Resource(name = "ticketMasBeanPopulator")
    SdTicketMasBeanPopulator ticketMasBeanPopulator;
    @Resource(name = "ticketContactBeanPopulator")
    SdTicketContactBeanPopulator ticketContactBeanPopulator;
    @Resource(name = "ticketServiceBeanPopulator")
    SdTicketServiceBeanPopulator ticketServiceBeanPopulator;
    @Resource(name = "ticketRemarkBeanPopulator")
    SdTicketRemarkBeanPopulator ticketRemarkBeanPopulator;
    @Resource(name = "teamSummaryBeanPopulator")
    SdTeamSummaryBeanPopulator teamSummaryBeanPopulator;
    @Resource(name = "ticketUploadFileBeanPopulator")
    SdTicketUploadFileBeanPopulator ticketUploadFileBeanPopulator;
    @Resource(name = "closeCodeBeanPopulator")
    SdCloseCodeBeanPopulator sdCloseCodeBeanPopulator;
    @Resource(name = "outstandingFaultBeanPopulator")
    SdOutstandingFaultBeanPopulator outstandingFaultBeanPopulator;
    @Resource(name = "ticketTimePeriodSummaryBeanPopulator")
    SdTicketTimePeriodSummaryBeanPopulator ticketTimePeriodSummaryBeanPopulator;

    private static String LOGINID = "LOGINID";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createQueryTicket(String custCode, String serviceNo, String serviceType, String subsId,
                                 String searchKey, String searchValue, String custName) {
        // conversion
        serviceNo = removeAllBlank(serviceNo);
        searchValue = removeAllBlank(searchValue);
        if (SdServiceTypeBean.SERVICE_TYPE.SMART_METER.equals(serviceType)) {
            serviceNo = String.valueOf(Integer.parseInt(serviceNo));
            searchValue = String.valueOf(Integer.parseInt(searchValue));
        }

        // ticket
        SdTicketMasEntity ticketMasEntity = new SdTicketMasEntity();
        SdUserBean currentUserBean = (SdUserBean) userService.getCurrentUserBean();
        String userId = currentUserBean.getUserId();
        String primaryRoleId = currentUserBean.getPrimaryRoleId();
        ticketMasEntity.setCustCode(custCode);
        ticketMasEntity.setCreateby(userId);
        ticketMasEntity.setCallInCount(1);
        ticketMasEntity.setSearchKey(searchKey);
        ticketMasEntity.setSearchValue(searchValue);
        ticketMasEntity.setOwningRole(primaryRoleId);
        ticketMasEntity.setCustName(custName);
        ticketMasMapper.insertQueryTicket(ticketMasEntity);

        // service
        SdTicketServiceEntity serviceEntity = new SdTicketServiceEntity();
        serviceEntity.setCreateby(userId);
        serviceEntity.setModifyby(userId);
        serviceEntity.setServiceId(serviceNo);
        serviceEntity.setTicketMasId(ticketMasEntity.getTicketMasId());
        serviceEntity.setServiceTypeCode(serviceType);
        serviceEntity.setSubsId(subsId);
        ticketServiceMapper.insertServiceInfo(serviceEntity);

        // remark
        createTicketSysRemarks(ticketMasEntity.getTicketMasId(),
                String.format(SdTicketRemarkBean.REMARKS.STATUS_TO_OPEN, userId));
        return ticketMasEntity.getTicketMasId();
    }

    public String removeAllBlank(String str) {
        str = StringUtils.deleteWhitespace(str);
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(str);
        str = m.replaceAll("");
        return str;
    }

    @Override
    public Optional<SdTicketMasBean> getTicket(Integer ticketMasId) {
        SdTicketMasBean bean = new SdTicketMasBean();
        return Optional.ofNullable(ticketMasMapper.findTicketById(ticketMasId)).map(sdTicketMasEntity -> {
            ticketMasBeanPopulator.populate(sdTicketMasEntity, bean);
            return bean;
        });
    }

    @Override
    public void insertTicketContactInfo(Integer ticketMasId, String contactType, String contactName, String contactNumber, String contactEmail, String contactMobile) {
        String createBy = userService.getCurrentUserUserId();
        ticketContactMapper.insertTicketContactInfo(ticketMasId, contactType, contactName,
                sensitiveDataService.encryptFromStringSafe(contactMobile),
                sensitiveDataService.encryptFromStringSafe(contactEmail),
                sensitiveDataService.encryptFromStringSafe(contactNumber),
                createBy);
    }

    @Override
    public List<SdTicketContactBean> getContactInfo(Integer ticketMasId) {
        List<SdTicketContactBean> beanList = new ArrayList<>();
        ticketContactMapper.selectContactInfoByTicketMasId(ticketMasId).forEach(sdTicketContactEntity -> {
            SdTicketContactBean bean = new SdTicketContactBean();
            bean.setContactMobile(sensitiveDataService.decryptToStringSafe(sdTicketContactEntity.getContactMobile()));
            bean.setContactEmail(sensitiveDataService.decryptToStringSafe(sdTicketContactEntity.getContactEmail()));
            bean.setContactNumber(sensitiveDataService.decryptToStringSafe(sdTicketContactEntity.getContactNumber()));
            ticketContactBeanPopulator.populate(sdTicketContactEntity, bean);
            beanList.add(bean);
        });
        return beanList;
    }

    @Override
    public void removeContactInfoByTicketMasId(Integer ticketMasId) {
        ticketContactMapper.removeContactInfoByTicketMasId(ticketMasId);
    }

    /*@Override
    public Page<SdTicketMasBean> searchTicketList(Pageable pageable, LocalDate createDateFrom, LocalDate createDateTo,
                                                  String status, LocalDate completeDateFrom, LocalDate completeDateTo,
                                                  String createBy, String ticketMasId, String custCode,
                                                  String serviceNumber, String serviceNumberExact, String ticketType,
                                                  String serviceType, boolean isReport, List<String> owningRole) {
        List<SdTicketMasEntity> entityList;
        Integer totalCount;

        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        if (isReport) {
            SdUserBean currentUserBean = userService.getCurrentSdUserBean();
            owningRole = List.of(currentUserBean.getPrimaryRoleId());
        }

        if (StringUtils.isNotEmpty(ticketMasId)) {
            entityList = ticketMasMapper.searchTicketList(offset, pageSize,
                    null, null, null,
                    null, null, null,
                    ticketMasId, null, null, null, null,
                    null, null);
            totalCount = ticketMasMapper.searchTicketCount(
                    null, null, null,
                    null, null, null,
                    ticketMasId, null, null, null, null,
                    null, null);

        } else if (StringUtils.isNotEmpty(serviceNumberExact)) {
            entityList = ticketMasMapper.searchTicketList(offset, pageSize,
                    createDateFrom, createDateTo, status,
                    completeDateFrom, completeDateTo, createBy,
                    ticketMasId, custCode, null, serviceNumberExact, ticketType,
                    serviceType, owningRole);
            totalCount = ticketMasMapper.searchTicketCount(
                    createDateFrom, createDateTo, status,
                    completeDateFrom, completeDateTo, createBy,
                    ticketMasId, custCode, null, serviceNumberExact, ticketType,
                    serviceType, owningRole);

        } else {
            entityList = ticketMasMapper.searchTicketList(offset, pageSize,
                    createDateFrom, createDateTo, status,
                    completeDateFrom, completeDateTo, createBy,
                    ticketMasId, custCode, serviceNumber, null, ticketType,
                    serviceType, owningRole);
            totalCount = ticketMasMapper.searchTicketCount(
                    createDateFrom, createDateTo, status,
                    completeDateFrom, completeDateTo, createBy,
                    ticketMasId, custCode, serviceNumber, null, ticketType,
                    serviceType, owningRole);
        }

        return new PageImpl<>(buildTicketBeanList(entityList), pageable, totalCount);
    }

    @Override
    public Page<SdTicketMasBean> getMyTicket(Pageable pageable) {
        String createBy = userService.getCurrentUserUserId();
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        List<SdTicketMasEntity> entityList = ticketMasMapper.searchTicketList(
                offset, pageSize, null, null, null, null, null, createBy, null, null, null, null, null, null, null);
        Integer totalCount = ticketMasMapper.searchTicketCount(null, null, null, null, null, createBy, null, null, null, null, null, null, null);

        return new PageImpl<>(buildTicketBeanList(entityList), pageable, totalCount);
    }*/

    private List<SdTicketMasBean> buildTicketBeanList(List<SdTicketMasEntity> entityList) {
        List<SdTicketMasBean> beanList = new LinkedList<>();
        for (SdTicketMasEntity entity : entityList) {
            SdTicketMasBean bean = new SdTicketMasBean();
            ticketMasBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }
        return beanList;
    }

    @Override
    public List<SdTicketRemarkBean> getTicketRemarksByTicketId(Integer ticketMasId) {
        List<SdTicketRemarkEntity> entityList = ticketRemarkMapper.getTicketRemarksByTicketId(ticketMasId, null);
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<SdTicketRemarkBean> beanList = new LinkedList<>();
        for (SdTicketRemarkEntity entity : entityList) {
            SdTicketRemarkBean bean = new SdTicketRemarkBean();
            ticketRemarkBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }

        return beanList;
    }

    public void createTicketCustRemarks(Integer ticketMasId, String remarks) {
        String createby = userService.getCurrentUserUserId();
        createTicketRemarks(ticketMasId, SdTicketRemarkEntity.REMARKS_TYPE.CUSTOMER, remarks, createby);
    }

    public void createTicketSysRemarks(Integer ticketMasId, String remarks) {
        createTicketRemarks(ticketMasId, SdTicketRemarkEntity.REMARKS_TYPE.SYSTEM, remarks, SdUserEntity.SYSTEM.USER_ID);
    }

    private void createTicketRemarks(Integer ticketMasId, String remarksType, String remarks, String createby) {
        ticketRemarkMapper.insertTicketRemarks(ticketMasId, remarksType, remarks, createby);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJobIdInService(Integer jobId, int ticketMasId, String userId) {
        ticketServiceMapper.updateTicketServiceByJobId(jobId, ticketMasId, userId);
//        ticketMasMapper.updateTicketStatus(ticketMasId, TicketStatusEnum.WORKING.getStatusCode(), null, null, userId);
        createTicketSysRemarks(ticketMasId, String.format(SdTicketRemarkBean.REMARKS.STATUS_TO_WORKING, userId));
    }

    @Override
    public List<SdSymptomBean> getSymptomList(Integer ticketMasId) {
        List<SdSymptomEntity> symptomEntities = ticketServiceMapper.getSymptomListByTicketMasId(ticketMasId);

        if (CollectionUtils.isEmpty(symptomEntities)) {
            List<SdSymptomEntity> allSymptomList = symptomMapper.getAllSymptomList();
            if (CollectionUtils.isEmpty(allSymptomList)) {
                return null;
            } else {
                symptomEntities = allSymptomList;
            }
        }

        return symptomEntities.stream().map(entity -> {
            SdSymptomBean bean = new SdSymptomBean();
            ticketServiceBeanPopulator.populate(entity, bean);
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SdTicketServiceBean> findServiceBySubscriberId(String subscriberId, Pageable pageable) {
        Long offset = null;
        Integer pageSize = null;
        if (pageable != null) {
            offset = pageable.getOffset();
            pageSize = pageable.getPageSize();
        }
        return ticketServiceMapper.getTicketServiceBySubscriberId(subscriberId, offset, pageSize)
                .stream().map(sdTicketServiceEntity -> {
                    SdTicketServiceBean bean = new SdTicketServiceBean();
                    ticketServiceBeanPopulator.populate(sdTicketServiceEntity, bean);
                    return bean;
                }).collect(Collectors.toList());
    }

    @Override
    public long countServiceBySubscriberId(String subscriberId) {
        return ticketServiceMapper.countServiceBySubscriberId(subscriberId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void increaseCallInCount(Integer ticketMasId) {
        // update count
        String userId = userService.getCurrentUserBean().getUserId();
        ticketMasMapper.updateTicketCallInCount(ticketMasId, userId);

        // add remarks
        createTicketCustRemarks(ticketMasId, SdTicketRemarkBean.REMARKS.CUSTOMER_CALL_IN);
    }

    @Override
    public List<SdTicketServiceBean> getServiceInfo(Integer ticketMasId) {
        List<SdTicketServiceEntity> serviceList = ticketServiceMapper.getTicketServiceInfoByTicketMasId(ticketMasId);

        if (CollectionUtils.isEmpty(serviceList)) {
            return null;
        }

        return serviceList.stream().map(entity -> {
            SdTicketServiceBean bean = new SdTicketServiceBean();
            ticketServiceBeanPopulator.populate(entity, bean);
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateServiceSymptom(Integer ticketMasId, String symptomCode, LocalDateTime reportTime) {
        BtuUserBean currentUserBean = userService.getCurrentUserBean();
        String modifyby = currentUserBean.getUserId();

        ticketServiceMapper.updateTicketServiceSymptomByTicketMasId(ticketMasId, symptomCode, modifyby, reportTime);
    }

    @Override
    public List<SdTicketMasBean> getPendingTicketList(String serviceType, String serviceNo) {
        if (StringUtils.isEmpty(serviceType)) {
            LOG.warn("Cannot get pending ticket without service type.");
            return null;
        }

        List<SdTicketMasEntity> entityList;
        switch (serviceType) {
            case SdServiceTypeBean.SERVICE_TYPE.ENTERPRISE_CLOUD:
            case SdServiceTypeBean.SERVICE_TYPE.ENTERPRISE_CLOUD_365:
            case SdServiceTypeBean.SERVICE_TYPE.VOIP:
            case SdServiceTypeBean.SERVICE_TYPE.FIX_NUMBER:
            case SdServiceTypeBean.SERVICE_TYPE.BROADBAND:
                entityList = ticketMasMapper.getTicketByServiceNo(serviceType, serviceNo, "JOB", TicketStatusEnum.COMPLETE.getStatusCode());
                break;
            case SdServiceTypeBean.SERVICE_TYPE.SMART_METER:
            default:
                entityList = ticketMasMapper.getTicketByServiceNo(serviceType, serviceNo, null, TicketStatusEnum.COMPLETE.getStatusCode());
                break;
        }

        return CollectionUtils.isEmpty(entityList) ? null : buildTicketBeanList(entityList);
    }

    @Override
    public boolean isMatchTicketJobType(Integer ticketMasId, TicketTypeEnum ticketTypeEnum) {
        if (ticketMasId == null) {
            LOG.warn("Null ticketMasId.");
            return false;
        }

        Page<SdTicketMasBean> pageResult = null;
        return pageResult.getTotalElements() > 0;
    }

    @Override
    public boolean isMatchTicketServiceType(Integer ticketMasId, String ticketServiceType) {
        if (ticketMasId == null) {
            LOG.warn("Null ticketMasId.");
            return false;
        }

        Page<SdTicketMasBean> pageResult = null;
        return pageResult.getTotalElements() > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeTicket(int ticketMasId, String closeCode, String reasonType, String reasonContent, LocalDateTime arrivalTime,
                            String contactName, String contactNumber, List<WfmCompleteInfo> wfmCompleteInfoList,
                            boolean nonApiClose) throws InvalidInputException {
        if (StringUtils.isEmpty(reasonType)) {
            throw new InvalidInputException("Empty reasonType.");
        } else if (StringUtils.isEmpty(reasonContent)) {
            throw new InvalidInputException("Empty reasonContent.");
        } else if (StringUtils.isBlank(contactName)) {
            throw new InvalidInputException("Empty contact name.");
        }

        SdUserBean currentUserBean = (SdUserBean) userService.getCurrentUserBean();

        // check ticket
        getTicket(ticketMasId).ifPresentOrElse(sdTicketMasBean -> {
            // check ticket status
            Optional.ofNullable(sdTicketMasBean.getStatus()).ifPresentOrElse(ticketStatusEnum -> {
                if (ticketStatusEnum.equals(TicketStatusEnum.COMPLETE)) {
                    throw new InvalidInputException("Ticket already closed.");
                }
            },() -> {
                throw new InvalidInputException("Ticket status not found.");
            });

            // check ticket ownership (for servicedesk close only)
            if (nonApiClose) {
                try {
                    // check create ticket owning role
                    userRoleService.checkUserRole(currentUserBean.getAuthorities(), List.of(sdTicketMasBean.getOwningRole()), false);
                } catch (InsufficientAuthorityException e) {
                    String th_role = "";
                    try {
                        // check create ticket owning role of team head
                        if (!sdTicketMasBean.getOwningRole().contains(SdUserRoleEntity.TEAM_HEAD_INDICATOR)
                                && (sdTicketMasBean.getOwningRole().contains(SdUserRoleEntity.ENGINEER_INDICATOR)
                                || sdTicketMasBean.getOwningRole().contains(SdUserRoleEntity.OPERATOR_INDICATOR))) {
                            th_role = SdUserRoleEntity.TEAM_HEAD_INDICATOR + sdTicketMasBean.getOwningRole();
                        }
                        userRoleService.checkUserRole(currentUserBean.getAuthorities(), List.of(th_role), false);
                    } catch (InsufficientAuthorityException e1) {
                        /*try {
                            // check auth role mapping
                            List<String> ticketAuth = userOwnerAuthRoleMapper.getUserOwnerAuthRole(sdTicketMasBean.getOwningRole()).stream()
                                    .map(SdUserOwnerAuthRoleEntity::getAuthRoleId).collect(Collectors.toList());
                            userRoleService.checkUserRole(currentUserBean.getAuthorities(), ticketAuth, false);
                        } catch (InsufficientAuthorityException e2) {
                            LOG.warn(e2.getMessage());
                            throw new InvalidInputException("This ticket belongs to another team (" + sdTicketMasBean.getOwningRole() + ").");
                        }*/
                    }
                }
            }

        },() -> {
            throw new InvalidInputException(String.format("Ticket not found. (ticketMasId: %d)", ticketMasId));
        });

        // close ticket
        String userUserId = userService.getCurrentUserUserId();
//        ticketMasMapper.updateTicketStatus(ticketMasId, TicketStatusEnum.COMPLETE.getStatusCode(), arrivalTime, LocalDateTime.now(), userUserId);

        // add WFM complete info
        if (CollectionUtils.isNotEmpty(wfmCompleteInfoList)) {
            String wfmCompleteInfo = new Gson().toJson(wfmCompleteInfoList);
            ticketServiceMapper.updateTicketServiceWfmCompInfo(ticketMasId, wfmCompleteInfo, userUserId);
        }

        // add close code
        if (StringUtils.isNotEmpty(closeCode)) {
            ticketServiceMapper.updateTicketServiceCloseCode(ticketMasId, closeCode, userUserId);
        }

        // add ticket remarks
        String content = String.format(SdTicketRemarkBean.REMARKS.STATUS_TO_CLOSE, arrivalTime, reasonType, reasonContent, contactName, contactNumber);
        createTicketSysRemarks(ticketMasId, content);

        LOG.info(String.format("Closed ticket. (ticketMasId: %d)", ticketMasId));
    }

    @Override
    public void updateTicketType(int ticketMasId, String type, String userId) {
        ticketMasMapper.updateTicketType(ticketMasId, type, userId);
    }

    @Override
    public List<SdCloseCodeBean> getCloseCodeList(String serviceType) {
        List<SdCloseCodeEntity> closeCodeEntities = sdCloseCodeMapper.getCloseCodeListByServiceType(serviceType);

        if (CollectionUtils.isEmpty(closeCodeEntities)) {
            return null;
        }

        return closeCodeEntities.stream().map(entity -> {
            SdCloseCodeBean bean = new SdCloseCodeBean();
            sdCloseCodeBeanPopulator.populate(entity, bean);
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    public void removeUploadFileByTicketMasId(Integer ticketMasId) {
        ticketFileUploadMapper.removeUploadFileByTicketMasId(ticketMasId);
    }

    @Override
    public Page<SdTicketMasBean> searchBchspList(Pageable pageable, LocalDate createDateFrom, LocalDate createDateTo, String status, LocalDate completeDateFrom, LocalDate completeDateTo, String createBy, String ticketMasId, String custCode, String serviceNumber, String ticketType, String serviceType, String owningRole, String workGroup) {
        List<SdTicketMasEntity> entityList;
        Integer totalCount;

        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        if (StringUtils.isNotEmpty(ticketMasId)) {
            entityList = ticketMasMapper.searchBchspList(offset, pageSize,
                    null, null, null,
                    null, null, null,
                    ticketMasId, null, null, null,
                    null, null, null);
            totalCount = ticketMasMapper.searchBchspCount(
                    null, null, null,
                    null, null, null,
                    ticketMasId, null, null, null,
                    null, null, null);
        } else {
            entityList = ticketMasMapper.searchBchspList(offset, pageSize,
                    createDateFrom, createDateTo, status,
                    completeDateFrom, completeDateTo, createBy,
                    ticketMasId, custCode, serviceNumber, ticketType,
                    serviceType, owningRole, workGroup);
            totalCount = ticketMasMapper.searchBchspCount(
                    createDateFrom, createDateTo, status,
                    completeDateFrom, completeDateTo, createBy,
                    ticketMasId, custCode, serviceNumber, ticketType,
                    serviceType, owningRole, workGroup);
        }

        return new PageImpl<>(buildTicketBeanList(entityList), pageable, totalCount);
    }

    @Override
    public String getJobId(String ticketMasId) {
        if (StringUtils.isEmpty(ticketMasId)) {
            return null;
        }

        return ticketMasMapper.getJobId(ticketMasId);
    }

    @Override
    public List<String> getWorkGroupList() {
        return ticketMasMapper.getWorkGroupList();
    }

    @Override
    public List<TicketStatusEnum> getTicketStatusList() {
        return Arrays.asList(TicketStatusEnum.values());
    }

    @Override
    public List<TicketTypeEnum> getTicketTypeList() {
        return Arrays.asList(TicketTypeEnum.values());
    }

    @Override
    public SdTeamSummaryBean getTeamSummary() {
        SdUserBean currentUserBean = userService.getCurrentSdUserBean();
        String owningRole = currentUserBean.getPrimaryRoleId();

        SdTeamSummaryBean teamSummaryBean = new SdTeamSummaryBean();

        // get data
        List<StatusSummaryEntity> countStatus = ticketMasMapper.getCountStatusByTicketType(owningRole);
        StatusSummaryEntity sumStatus = ticketMasMapper.getSumStatusByTicketType(owningRole);

        // set each status summary
        countStatus.forEach(entity -> teamSummaryBeanPopulator.populate(entity, teamSummaryBean));

        // set team summary
        Optional.ofNullable(sumStatus).ifPresent(statusSummaryEntity -> teamSummaryBeanPopulator.populate(statusSummaryEntity, teamSummaryBean));

        return teamSummaryBean;
    }

    @Override
    public SdMakeApptBean getTicketServiceByDetId(Integer ticketDetId) {
        if (ticketDetId <= 0) {
            return null;
        }

        SdMakeApptEntitiy entity = ticketServiceMapper.getTicketServiceByTicketDetId(ticketDetId);

        if (entity == null) {
            return null;
        }

        SdMakeApptBean bean = new SdMakeApptBean();
        bean.setBsn(entity.getBsn());
        bean.setServiceType(entity.getServiceType());
        bean.setSymptomCode(entity.getSymptomCode());
        bean.setTicketMasId(entity.getTicketMasId());
        bean.setTicketDetId(entity.getTicketDetId());

        return bean;
    }

    @Override
    public String getNewTicketId() {
        return ticketMasMapper.getNewTicketId();
    }

    @Override
    public void createHktCloudTicket(int ticketId, String tenantId, String createdBy) {
        ticketServiceMapper.insertServiceInfoForCloudCase(ticketId, createdBy, tenantId);
        ticketMasMapper.createHktCloudTicket(ticketId, createdBy, tenantId);
    }

    @Override
    public void insertUploadFile(int ticketId, String fileName, String content) {
        ticketFileUploadMapper.insertUploadFile(ticketId, fileName, Base64.getDecoder().decode(content));
    }

    @Override
    public List<SdTicketMasBean> getHktCloudTicket(String tenantId, String username) {
        return ticketMasMapper.getTicket4HktCloud(tenantId, username).stream().map(sdTicketMasEntity -> {
            SdTicketMasBean bean = new SdTicketMasBean();
            ticketMasBeanPopulator.populate(sdTicketMasEntity, bean);
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SdTicketUploadFileBean> getUploadFiles(int ticketMasId) {
        return ticketFileUploadMapper.getUploadFiles(ticketMasId).stream().map(entity -> {
            SdTicketUploadFileBean bean = new SdTicketUploadFileBean();
            ticketUploadFileBeanPopulator.populate(entity, bean);
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SdTicketServiceBean> getCloseInfo(Integer ticketMasId) {
        List<SdTicketServiceEntity> serviceList = ticketServiceMapper.getTicketServiceInfoByTicketMasId(ticketMasId);

        if (CollectionUtils.isEmpty(serviceList)) {
            return null;
        }

        return serviceList.stream().map(entity -> {
            SdTicketServiceBean bean = new SdTicketServiceBean();
            bean.setWfmCompleteInfo(entity.getWfmCompleteInfo());
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SdSymptomWorkingPartyMappingBean> getSymptomByServiceType(String serviceType, String workingParty) {
        List<SdSymptomWorkingPartyMappingEntity> symptomEntities = symptomMapper.getSymptomByServiceType(serviceType, workingParty);

        if (symptomEntities == null) {
            return null;
        }

        return symptomEntities.stream().map(entity -> {
            SdSymptomWorkingPartyMappingBean bean = new SdSymptomWorkingPartyMappingBean();
            ticketServiceBeanPopulator.populate(entity, bean);
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteEntityVarchar(Integer ticketMasId) {
        ticketOtherMapper.deleteEntityVarchar(ticketMasId);
    }

    @Override
    public void insertExtraInfo(Integer ticketMasId, String parameterName, String parameterValue) {
        if (StringUtils.isEmpty(parameterName)) {
            LOG.info(String.format("Attribute name (%s) is null.", parameterName));
            return;
        } else if (StringUtils.isEmpty(parameterValue)) {
            LOG.info(String.format("Attribute value (%s) is null.", parameterValue));
            return;
        }

        SdEvaAttributeEntity entity = ticketOtherMapper.getAttributeIdByName(parameterName);
        if (entity == null) {
            LOG.info(String.format("Attribute ID not found by attribute name: %s.", parameterName));
            return;
        }

        String createby = userService.getCurrentUserUserId();
        ticketOtherMapper.insertEntityVarchar(ticketMasId, entity.getAttributeId(), parameterValue, createby);
    }

    @Override
    public SdGmbTicketEavBean getGmbTicketOtherInfo(Integer ticketMasId) {
        SdGmbTicketEavEntity entity = ticketOtherMapper.getGmbTicketOtherInfo(ticketMasId);

        if (entity == null) {
            return null;
        }

        SdGmbTicketEavBean bean = new SdGmbTicketEavBean();
        ticketServiceBeanPopulator.populate(entity, bean);
        return bean;
    }

    @Override
    public List<SdTicketExportBean> searchTicketListForExport(LocalDate createDateFrom, LocalDate createDateTo, String status,
                                                              LocalDate completeDateFrom, LocalDate completeDateTo, String createBy,
                                                              String ticketMasId, String serviceNumber, String ticketType,
                                                              Integer priority) {
        List<SdTicketExportEntity> entityList = ticketMasMapper.searchTicketListForExport(createDateFrom, createDateTo, status,
                completeDateFrom, completeDateTo, createBy, ticketMasId, serviceNumber, ticketType, priority);

        List<SdTicketExportBean> beanList = new LinkedList<>();
        for (SdTicketExportEntity entity : entityList) {
            SdTicketExportBean bean = new SdTicketExportBean();
            ticketMasBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }

        return beanList;
    }

    @Override
    @Transactional
    public void assignTicket(int ticketMasId, String engineer, String remark) {
        if (StringUtils.isEmpty(engineer)) {
            throw new InvalidInputException("Empty engineer.");
        }

        SdUserBean currentUserBean = (SdUserBean) userService.getCurrentUserBean();

        // check ticket
        getTicket(ticketMasId).ifPresentOrElse(sdTicketMasBean -> {
            // check ticket status
            Optional.ofNullable(sdTicketMasBean.getStatus()).ifPresentOrElse(ticketStatusEnum -> {
                if (ticketStatusEnum.equals(TicketStatusEnum.WORKING)) {
                    throw new InvalidInputException("Ticket already assigned.");
                }
            },() -> {
                throw new InvalidInputException("Ticket status not found.");
            });
        },() -> {
            throw new InvalidInputException(String.format("Ticket not found. (ticketMasId: %d)", ticketMasId));
        });

        // assign ticket
        String userUserId = userService.getCurrentUserUserId();
        ticketMasMapper.updateTicketAssign(ticketMasId, TicketStatusEnum.WORKING.getStatusCode(), engineer, userUserId);

        // add ticket remarks
        String assignRemark = String.format(SdTicketRemarkBean.REMARKS.STATUS_TO_WORKING, engineer, userUserId);
        createTicketRemarks(ticketMasId, SdTicketRemarkEntity.REMARKS_TYPE.ASSIGN, assignRemark, currentUserBean.getUserId());
        if (StringUtils.isNotEmpty(remark)) {
            createTicketRemarks(ticketMasId, SdTicketRemarkEntity.REMARKS_TYPE.ASSIGN, remark, currentUserBean.getUserId());
        }

        LOG.info(String.format("Assigned ticket. (ticketMasId: %d)", ticketMasId));
    }

    @Override
    public void completeTicket(int ticketMasId, String remark) {
        if (StringUtils.isEmpty(remark)) {
            throw new InvalidInputException("Empty remark.");
        }

        // check ticket
        getTicket(ticketMasId).ifPresentOrElse(sdTicketMasBean -> {
            // check ticket status
            Optional.ofNullable(sdTicketMasBean.getStatus()).ifPresentOrElse(ticketStatusEnum -> {
                if (ticketStatusEnum.equals(TicketStatusEnum.COMPLETE)) {
                    throw new InvalidInputException("Ticket already completed.");
                }
            },() -> {
                throw new InvalidInputException("Ticket status not found.");
            });
        },() -> {
            throw new InvalidInputException(String.format("Ticket not found. (ticketMasId: %d)", ticketMasId));
        });

        // assign ticket
        String userUserId = userService.getCurrentUserUserId();
        ticketMasMapper.updateTicketComplete(ticketMasId, TicketStatusEnum.COMPLETE.getStatusCode(), userUserId);

        // add ticket remarks
        if (StringUtils.isNotEmpty(remark)) {
            createTicketRemarks(ticketMasId, SdTicketRemarkEntity.REMARKS_TYPE.COMPLETE, remark, userUserId);
        }

        LOG.info(String.format("Completed ticket. (ticketMasId: %d)", ticketMasId));
    }

    @Override
    public List<SdOutstandingFaultBean> getOutstandingFault() {
        List<SdOutstandingFaultEntity> entityList = ticketMasMapper.getOutstandingFault();
        return entityList.stream()
                .filter(entity -> !entity.getServiceTypeCode().equals(LOGINID))
                .map(entity -> {
                    SdOutstandingFaultBean bean = new SdOutstandingFaultBean();
                    outstandingFaultBeanPopulator.populate(entity, bean);
                    return bean;
                }).collect(Collectors.toList());
    }

    @Override
    public List<SdTicketTimePeriodSummaryBean> getTicketTimePeriodSummary() {
        return ticketMasMapper.getTicketTimePeriodSummary().stream().map(entity -> {
            SdTicketTimePeriodSummaryBean bean = new SdTicketTimePeriodSummaryBean();
            ticketTimePeriodSummaryBeanPopulator.populate(entity, bean);
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    public String getAvgFaultCleaningTime(){
        String avgFaultCleaningTime;
        try {
            Double time = ticketMasMapper.getAvgFaultCleaningTime();
            avgFaultCleaningTime = String.format("%.2f", time);
        } catch (Exception e) {
            return null;
        }
        return avgFaultCleaningTime;
    }

    @Override
    public List<SdTicketRemarkBean> getTicketCustRemarks(Integer ticketMasId) {
        List<SdTicketRemarkEntity> entityList = ticketRemarkMapper.getTicketRemarksByTicketId(ticketMasId, SdTicketRemarkEntity.REMARKS_TYPE.CUSTOMER);
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<SdTicketRemarkBean> beanList = new LinkedList<>();
        for (SdTicketRemarkEntity entity : entityList) {
            SdTicketRemarkBean bean = new SdTicketRemarkBean();
            ticketRemarkBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }

        return beanList;
    }

    @Override
    public int createTicket(String serviceNumber, String ticketType, Integer priority, String remarks) {
        // ticket
        SdTicketMasEntity ticketMasEntity = new SdTicketMasEntity();
        SdUserBean currentUserBean = (SdUserBean) userService.getCurrentUserBean();
        String userId = currentUserBean.getUserId();
        ticketMasEntity.setServiceNumber(serviceNumber);
        ticketMasEntity.setTicketType(ticketType);
        ticketMasEntity.setPriority(priority);
        ticketMasEntity.setCreateby(userId);
        ticketMasMapper.insertTicket(ticketMasEntity);

        // remark
        createTicketRemarks(ticketMasEntity.getTicketMasId(), SdTicketRemarkEntity.REMARKS_TYPE.CREATE, remarks, currentUserBean.getUserId());
        return ticketMasEntity.getTicketMasId();
    }

    @Override
    @Transactional
    public void closeTicket(int ticketMasId, String reasonContent) throws InvalidInputException {
        if (StringUtils.isEmpty(reasonContent)) {
            throw new InvalidInputException("Please input reason.");
        }

        // check ticket
        getTicket(ticketMasId).ifPresentOrElse(sdTicketMasBean -> {
            // check ticket status
            Optional.ofNullable(sdTicketMasBean.getStatus()).ifPresentOrElse(ticketStatusEnum -> {
                if (ticketStatusEnum.equals(TicketStatusEnum.COMPLETE)) {
                    throw new InvalidInputException("Ticket already closed.");
                }
            },() -> {
                throw new InvalidInputException("Ticket status not found.");
            });
        },() -> {
            throw new InvalidInputException(String.format("Ticket not found. (ticketMasId: %d)", ticketMasId));
        });

        // close ticket
        String userUserId = userService.getCurrentUserUserId();
        ticketMasMapper.updateTicketStatus(ticketMasId, TicketStatusEnum.CLOSE.getStatusCode(), userUserId);

        // add ticket remarks
        createTicketRemarks(ticketMasId, SdTicketRemarkEntity.REMARKS_TYPE.CLOSE, reasonContent, userUserId);

        LOG.info(String.format("Closed ticket. (ticketMasId: %d)", ticketMasId));
    }

    @Override
    public Page<SdTicketMasBean> searchTicketList(Pageable pageable, LocalDate createDateFrom,
                                                  LocalDate createDateTo, String status,
                                                  LocalDate completeDateFrom, LocalDate completeDateTo,
                                                  String createBy, String ticketMasId, String serviceNumber,
                                                  String ticketType, Integer priority) {
        List<SdTicketMasEntity> entityList;
        Integer totalCount;

        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        if (StringUtils.isNotEmpty(ticketMasId)) {
            entityList = ticketMasMapper.searchTicketList(offset, pageSize,
                    null, null, null,
                    null, null, null,
                    ticketMasId, null, null, null);
            totalCount = ticketMasMapper.searchTicketCount(
                    null, null, null,
                    null, null, null,
                    ticketMasId, null, null, null);
        } else {
            entityList = ticketMasMapper.searchTicketList(offset, pageSize,
                    createDateFrom, createDateTo, status,
                    completeDateFrom, completeDateTo, createBy,
                    ticketMasId, serviceNumber, ticketType, priority);
            totalCount = ticketMasMapper.searchTicketCount(
                    createDateFrom, createDateTo, status,
                    completeDateFrom, completeDateTo, createBy,
                    ticketMasId, serviceNumber, ticketType, priority);
        }

        return new PageImpl<>(buildTicketBeanList(entityList), pageable, totalCount);
    }
}
