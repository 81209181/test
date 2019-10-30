package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.BtuSensitiveDataService;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.sd.core.dao.entity.*;
import com.hkt.btu.sd.core.dao.mapper.SdTicketContactMapper;
import com.hkt.btu.sd.core.dao.mapper.SdTicketMasMapper;
import com.hkt.btu.sd.core.dao.mapper.SdTicketRemarkMapper;
import com.hkt.btu.sd.core.dao.mapper.SdTicketServiceMapper;
import com.hkt.btu.sd.core.service.SdTicketService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.*;
import com.hkt.btu.sd.core.service.populator.SdTicketContactBeanPopulator;
import com.hkt.btu.sd.core.service.populator.SdTicketMasBeanPopulator;
import com.hkt.btu.sd.core.service.populator.SdTicketRemarkBeanPopulator;
import com.hkt.btu.sd.core.service.populator.SdTicketServiceBeanPopulator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SdTicketServiceImpl implements SdTicketService {

    @Resource
    SdTicketMasMapper ticketMasMapper;
    @Resource
    SdTicketContactMapper ticketContactMapper;
    @Resource
    SdTicketServiceMapper ticketServiceMapper;
    @Resource
    SdTicketRemarkMapper ticketRemarkMapper;

    @Resource(name = "userService")
    SdUserService userService;
    @Resource(name = "sensitiveDataService")
    BtuSensitiveDataService sensitiveDataService;

    @Resource(name = "ticketMasBeanPopulator")
    SdTicketMasBeanPopulator ticketMasBeanPopulator;
    @Resource(name = "ticketContactBeanPopulator")
    SdTicketContactBeanPopulator ticketContactBeanPopulator;
    @Resource(name = "ticketServiceBeanPopulator")
    SdTicketServiceBeanPopulator ticketServiceBeanPopulator;
    @Resource(name = "ticketRemarkBeanPopulator")
    SdTicketRemarkBeanPopulator ticketRemarkBeanPopulator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createQueryTicket(String custCode, String serviceNo, String serviceType, String subsId,
                                 String searchKey, String searchValue) {
        SdTicketMasEntity ticketMasEntity = new SdTicketMasEntity();
        String userId = userService.getCurrentUserUserId();
        ticketMasEntity.setCustCode(custCode);
        ticketMasEntity.setCreateby(userId);
        ticketMasEntity.setCallInCount(1);
        ticketMasEntity.setSearchKey(searchKey);
        ticketMasEntity.setSearchValue(searchValue);
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
    public Optional<SdTicketMasBean> getTicket(Integer ticketId) {
        SdTicketMasBean bean = new SdTicketMasBean();
        return Optional.ofNullable(ticketMasMapper.findTicketById(ticketId)).map(sdTicketMasEntity -> {
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
            bean.setContactMobile(sensitiveDataService.decryptToStringSafe(sdTicketContactEntity.getContactEmail()));
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
    public Page<SdTicketMasBean> searchTicketList(Pageable pageable, String dateFrom, String dateTo, String status, String ticketMasId, String custCode) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        List<SdTicketMasEntity> entityList = ticketMasMapper.searchTicketList(
                offset, pageSize, dateFrom, dateTo, status, ticketMasId, custCode, null);
        Integer totalCount = ticketMasMapper.searchTicketCount(dateFrom, dateTo, status, ticketMasId, custCode, null);

        return new PageImpl<>(populateBeanList(entityList), pageable, totalCount);
    }

    @Override
    public Page<SdTicketMasBean> getMyTicket(Pageable pageable) {
        String createBy = userService.getCurrentUserUserId();
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        List<SdTicketMasEntity> entityList = ticketMasMapper.searchTicketList(
                offset, pageSize, null, null, null, null, null, createBy);
        Integer totalCount = ticketMasMapper.searchTicketCount(null, null, null, null, null, createBy);

        return new PageImpl<>(populateBeanList(entityList), pageable, totalCount);
    }

    private List<SdTicketMasBean> populateBeanList(List<SdTicketMasEntity> entityList){
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
        ticketMasMapper.updateTicketStatus(ticketMasId, SdTicketMasBean.STATUS_TYPE_CODE.WORKING, userId);
        createTicketSysRemarks(ticketMasId, String.format(SdTicketRemarkBean.REMARKS.STATUS_TO_WORKING, userId));
    }

    @Override
    public Optional<SdTicketServiceBean> getService(Integer ticketId) {
        return Optional.ofNullable(ticketServiceMapper.getTicketServiceByTicketMasId(ticketId)).map(sdTicketServiceEntity -> {
            SdTicketServiceBean bean = new SdTicketServiceBean();
            ticketServiceBeanPopulator.populate(sdTicketServiceEntity, bean);
            return bean;
        });
    }

    @Override
    public void updateAppointment(String appointmentDate, boolean asap, String userId, String ticketMasId) {
        ticketMasMapper.updateAppointmentInMas(LocalDateTime.parse(appointmentDate), asap ? "Y" : "N", userId, ticketMasId);
    }

    @Override
    public boolean checkAppointmentDate(String appointmentDate) {
        return LocalDateTime.now().plusHours(2).plusMinutes(-1).isBefore(LocalDateTime.parse(appointmentDate));
    }

    @Override
    public List<SdSymptomBean> getSymptomList(Integer ticketMasId) {
        List<SdSymptomEntity> symptomEntities = ticketServiceMapper.getSymptomListByTicketMasId(ticketMasId);

        if (CollectionUtils.isEmpty(symptomEntities)) {
            List<SdSymptomEntity> allSymptomList = ticketServiceMapper.getAllSymptomList();
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
    public List<SdTicketServiceBean> findServiceBySubscriberId(String subscriberId) {
        return ticketServiceMapper.getTicketServiceBySubscriberId(subscriberId).stream().map(sdTicketServiceEntity -> {
            SdTicketServiceBean bean = new SdTicketServiceBean();
            ticketServiceBeanPopulator.populate(sdTicketServiceEntity, bean);
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTicketStatus(int ticketMasId, String status, String userId) {
        ticketMasMapper.updateTicketStatus(ticketMasId, status, userId);
        createTicketSysRemarks(ticketMasId, "Cancel ticket.");
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
    public void removeServiceInfoByTicketMasId(Integer ticketMasId) {
        ticketServiceMapper.removeServiceInfoByTicketMasId(ticketMasId);
        List<SdTicketServiceEntity> serviceInfoList = ticketServiceMapper.getTicketServiceInfoByTicketMasId(ticketMasId);
        if (CollectionUtils.isNotEmpty(serviceInfoList)) {
            List<Integer> ticketServiceIds = serviceInfoList.stream().map(SdTicketServiceEntity::getTicketDetId).collect(Collectors.toList());
            ticketServiceIds.forEach(ticketDetId -> ticketServiceMapper.removeFaultsByTicketDetId(ticketDetId));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public int updateServiceInfo(SdTicketServiceBean bean) {
        BtuUserBean currentUserBean = userService.getCurrentUserBean();
        String createby = currentUserBean.getUserId();

        SdTicketServiceEntity entity = new SdTicketServiceEntity();
        entity.setCreateby(createby);
        entity.setModifyby(createby);
        entity.setServiceId(bean.getServiceId());
        entity.setTicketMasId(bean.getTicketMasId());
        entity.setServiceTypeCode(bean.getServiceTypeCode());

        ticketServiceMapper.insertServiceInfo(entity);
        return entity.getTicketDetId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateServiceSymptom(Integer ticketMasId, String symptomCode) {
        BtuUserBean currentUserBean = userService.getCurrentUserBean();
        String modifyby = currentUserBean.getUserId();

        ticketServiceMapper.updateTicketServiceSymptomByTicketMasId(ticketMasId, symptomCode, modifyby);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateFaultsInfo(Integer ticketDetId, String faults) {
        BtuUserBean currentUserBean = userService.getCurrentUserBean();
        String createBy = currentUserBean.getUserId();

        ticketServiceMapper.insertFaults(ticketDetId, faults, createBy, createBy);
    }

    @Override
    public List<SdTicketMasBean> getTicketByServiceNo(String serviceNo, String status) {
        List<SdTicketMasEntity> entityList = ticketMasMapper.getTicketByServiceNo(serviceNo, status);
        return CollectionUtils.isEmpty(entityList) ? null : populateBeanList(entityList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeTicket(int ticketMasId, String reasonType, String reasonContent, String closeby) throws InvalidInputException {
        if (StringUtils.isEmpty(reasonType)) {
            throw new InvalidInputException("Empty reasonType.");
        } else if(StringUtils.isEmpty(reasonContent)) {
            throw new InvalidInputException("Empty reasonContent.");
        } else if (StringUtils.isEmpty(closeby)) {
            throw new InvalidInputException("Empty closeBy.");
        }

        // check status
        Optional<SdTicketMasBean> ticketMasBeanOptional = getTicket(ticketMasId);
        if(ticketMasBeanOptional.isPresent()){
            SdTicketMasBean sdTicketMasBean = ticketMasBeanOptional.get();
            String ticketStatus = sdTicketMasBean.getStatus();
            if( StringUtils.isEmpty(ticketStatus) ){
                throw new InvalidInputException("Ticket status not found.");
            } else if( SdTicketMasBean.STATUS_TYPE.COMPLETE.equals(ticketStatus) ){
                throw new InvalidInputException("Ticket already closed.");
            }
        } else {
            throw new InvalidInputException(String.format("Ticket not found. (ticketMasId: %d)", ticketMasId));
        }


        // close ticket and add remarks
        String modifyby = userService.getCurrentUserUserId();
        ticketMasMapper.updateTicketStatus(ticketMasId, SdTicketMasBean.STATUS_TYPE_CODE.COMPLETE, modifyby);
        createTicketSysRemarks(ticketMasId, String.format(SdTicketRemarkBean.REMARKS.STATUS_TO_CLOSE, reasonType, reasonContent, closeby));
    }

    @Override
    public void updateTicketType(int ticketMasId, String type, String userId) {
        ticketMasMapper.updateTicketType(ticketMasId,type,userId);
    }
}
