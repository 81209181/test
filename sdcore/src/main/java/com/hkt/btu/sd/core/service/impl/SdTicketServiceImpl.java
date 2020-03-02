package com.hkt.btu.sd.core.service.impl;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createQueryTicket(String custCode, String serviceNo, String serviceType, String subsId,
                                 String searchKey, String searchValue, String custName) {
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

    @Override
    public Page<SdTicketMasBean> searchTicketList(Pageable pageable, LocalDate createDateFrom, LocalDate createDateTo,
                                                  String status, LocalDate completeDateFrom, LocalDate completeDateTo,
                                                  String createBy, String ticketMasId, String custCode,
                                                  String serviceNumber, String ticketType, String serviceType, boolean isReport, String owningRole) {
        List<SdTicketMasEntity> entityList;
        Integer totalCount;

        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        if (isReport) {
            SdUserBean currentUserBean = userService.getCurrentSdUserBean();
            owningRole = currentUserBean.getPrimaryRoleId();
        }

        if (StringUtils.isNotEmpty(ticketMasId)) {
            entityList = ticketMasMapper.searchTicketList(offset, pageSize,
                    null, null, null,
                    null, null, null,
                    ticketMasId, null, null, null,
                    null,null);
            totalCount = ticketMasMapper.searchTicketCount(
                    null, null, null,
                    null, null, null,
                    ticketMasId, null, null, null,
                    null, null);
        } else {
            entityList = ticketMasMapper.searchTicketList(offset, pageSize,
                    createDateFrom, createDateTo, status,
                    completeDateFrom, completeDateTo, createBy,
                    ticketMasId, custCode, serviceNumber, ticketType,
                    serviceType, owningRole);
            totalCount = ticketMasMapper.searchTicketCount(
                    createDateFrom, createDateTo, status,
                    completeDateFrom, completeDateTo, createBy,
                    ticketMasId, custCode, serviceNumber, ticketType,
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
                offset, pageSize, null, null, null, null, null, createBy, null, null, null, null, null, null);
        Integer totalCount = ticketMasMapper.searchTicketCount(null, null, null, null, null, createBy, null, null, null, null, null, null);

        return new PageImpl<>(buildTicketBeanList(entityList), pageable, totalCount);
    }

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
        List<SdTicketRemarkEntity> entityList = ticketRemarkMapper.getTicketRemarksByTicketId(ticketMasId);
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
        ticketMasMapper.updateTicketStatus(ticketMasId, TicketStatusEnum.WORKING.getStatusCode(), userId);
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
        if (pageable!=null) {
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
    public List<SdTicketMasBean> getTicketByServiceNo(String serviceNo, String ticketType, String excludeStatus) {
        List<SdTicketMasEntity> entityList = ticketMasMapper.getTicketByServiceNo(serviceNo, ticketType, excludeStatus);
        return CollectionUtils.isEmpty(entityList) ? null : buildTicketBeanList(entityList);
    }

    @Override
    public List<SdTicketMasBean> getPendingTicketList(String serviceNo) {
        return getTicketByServiceNo(serviceNo, SdTicketMasEntity.TICKET_TYPE.JOB, SdTicketMasEntity.STATUS.COMPLETE);
    }

    @Override
    public boolean isMatchTicketJobType(Integer ticketMasId, TicketTypeEnum ticketTypeEnum) {
        if(ticketMasId==null){
            LOG.warn("Null ticketMasId.");
            return false;
        }

        Page<SdTicketMasBean> pageResult = searchTicketList(
                PageRequest.of(0, 10), null, null,
                null, null, null,
                null, String.valueOf(ticketMasId), null,
                null, ticketTypeEnum.getTypeCode(), null, false, null);
        return pageResult.getTotalElements() > 0;
    }

    @Override
    public boolean isMatchTicketServiceType(Integer ticketMasId, String ticketServiceType) {
        if(ticketMasId==null){
            LOG.warn("Null ticketMasId.");
            return false;
        }

        Page<SdTicketMasBean> pageResult = searchTicketList(
                PageRequest.of(0, 10), null, null,
                null, null, null,
                null, String.valueOf(ticketMasId), null,
                null, null, ticketServiceType, false, null);
        return pageResult.getTotalElements() > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeTicket(int ticketMasId, String reasonType, String reasonContent,
                            String contactName, String contactNumber, boolean nonApiClose) throws InvalidInputException {
        if (StringUtils.isEmpty(reasonType)) {
            throw new InvalidInputException("Empty reasonType.");
        } else if (StringUtils.isEmpty(reasonContent)) {
            throw new InvalidInputException("Empty reasonContent.");
        } else if (StringUtils.isBlank(contactName)) {
            throw new InvalidInputException("Empty contact name.");
        }

        // check ticket
        Optional<SdTicketMasBean> ticketMasBeanOptional = getTicket(ticketMasId);
        SdUserBean currentUserBean = (SdUserBean) userService.getCurrentUserBean();
        if (ticketMasBeanOptional.isPresent()) {
            SdTicketMasBean sdTicketMasBean = ticketMasBeanOptional.get();

            // check ticket status
            TicketStatusEnum ticketStatus = sdTicketMasBean.getStatus();
            if (ticketStatus == null) {
                throw new InvalidInputException("Ticket status not found.");
            } else if (ticketStatus == TicketStatusEnum.COMPLETE) {
                throw new InvalidInputException("Ticket already closed.");
            }

            // check ticket ownership (for servicedesk close only)
            if(nonApiClose) {
                String ticketOwningRole = sdTicketMasBean.getOwningRole();
                try {
                    userRoleService.checkUserRole(currentUserBean.getAuthorities(), List.of(ticketOwningRole));
                }catch (InsufficientAuthorityException e){
                    LOG.warn(e.getMessage());
                    throw new InvalidInputException("This ticket belongs to another team (" + ticketOwningRole + ").");
                }
            }
        } else {
            throw new InvalidInputException(String.format("Ticket not found. (ticketMasId: %d)", ticketMasId));
        }

        // close ticket
        String userUserId = userService.getCurrentUserUserId();
        ticketMasMapper.updateTicketStatus(ticketMasId, TicketStatusEnum.COMPLETE.getStatusCode(), userUserId);

        // add ticket remarks
        String content = String.format(SdTicketRemarkBean.REMARKS.STATUS_TO_CLOSE, reasonType, reasonContent, contactName, contactNumber);
        createTicketSysRemarks(ticketMasId, content);

        LOG.info(String.format("Closed ticket. (ticketMasId: %d)", ticketMasId));
    }

    @Override
    public void updateTicketType(int ticketMasId, String type, String userId) {
        ticketMasMapper.updateTicketType(ticketMasId, type, userId);
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
        countStatus.forEach(entity -> teamSummaryBeanPopulator.populate(entity,teamSummaryBean));

        // set team summary
        teamSummaryBeanPopulator.populate(sumStatus, teamSummaryBean);
        return teamSummaryBean;
    }

    @Override
    public SdMakeApptBean getTicketServiceByDetId(Integer ticketDetId) {
        if (ticketDetId <= 0 ) {
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
        ticketServiceMapper.insertServiceInfoForCloudCase(ticketId,createdBy,tenantId);
        ticketMasMapper.createHktCloudTicket(ticketId,createdBy,tenantId);
    }

    @Override
    public void insertUploadFile(int ticketId, String fileName, String content) {
        ticketFileUploadMapper.insertUploadFile(ticketId,fileName,Base64.getDecoder().decode(content));
    }

    @Override
    public List<SdTicketMasBean> getHktCloudTicket(String tenantId, String username) {
        return ticketMasMapper.getTicket4HktCloud(tenantId,username).stream().map(sdTicketMasEntity -> {
            SdTicketMasBean bean = new SdTicketMasBean();
            ticketMasBeanPopulator.populate(sdTicketMasEntity, bean);
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SdTicketUploadFileBean> getUploadFiles(int ticketMasId) {
        return ticketFileUploadMapper.getUploadFiles(ticketMasId).stream().map(entity -> {
            SdTicketUploadFileBean bean = new SdTicketUploadFileBean();
            ticketUploadFileBeanPopulator.populate(entity,bean);
            return bean;
        }).collect(Collectors.toList());
    }
}
