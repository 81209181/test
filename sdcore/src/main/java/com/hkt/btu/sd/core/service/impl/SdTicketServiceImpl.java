package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.sd.core.dao.entity.SdSymptomEntity;
import com.hkt.btu.sd.core.dao.entity.SdTicketMasEntity;
import com.hkt.btu.sd.core.dao.entity.SdTicketRemarkEntity;
import com.hkt.btu.sd.core.dao.entity.SdTicketServiceEntity;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
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
    public int createQueryTicket(String custCode, String serviceNo, String serviceType, String subsId) {
        SdTicketMasEntity ticketMasEntity = new SdTicketMasEntity();
        String userId = userService.getCurrentUserUserId();
        ticketMasEntity.setCustCode(custCode);
        ticketMasEntity.setCreateby(userId);
        ticketMasMapper.insertQueryTicket(ticketMasEntity);

        SdTicketServiceEntity entity = new SdTicketServiceEntity();
        entity.setCreateby(userId);
        entity.setModifyby(userId);
        entity.setServiceId(serviceNo);
        entity.setTicketMasId(ticketMasEntity.getTicketMasId());
        entity.setServiceTypeCode(serviceType);
        entity.setSubsId(subsId);
        ticketServiceMapper.insertServiceInfo(entity);
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
        ticketContactMapper.insertTicketContactInfo(ticketMasId, contactType, contactName, contactMobile, contactEmail, contactNumber, createBy);
    }

    @Override
    public List<SdTicketContactBean> getContactInfo(Integer ticketMasId) {
        List<SdTicketContactBean> beanList = new ArrayList<>();
        ticketContactMapper.selectContactInfoByTicketMasId(ticketMasId).forEach(sdTicketContactEntity -> {
            SdTicketContactBean bean = new SdTicketContactBean();
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
    public Page<SdTicketMasBean> searchTicketList(Pageable pageable, String dateFrom, String dateTo, String status) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        List<SdTicketMasEntity> entityList = ticketMasMapper.searchTicketList(offset, pageSize, dateFrom, dateTo, status);
        Integer totalCount = ticketMasMapper.searchTicketCount(dateFrom, dateTo, status);

        List<SdTicketMasBean> beanList = new LinkedList<>();
        for (SdTicketMasEntity entity : entityList) {
            SdTicketMasBean bean = new SdTicketMasBean();
            ticketMasBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }

        return new PageImpl<>(beanList, pageable, totalCount);
    }

    @Override
    public List<SdTicketMasBean> getMyTicket() {
        String createBy = userService.getCurrentUserUserId();
        List<SdTicketMasEntity> entityList = ticketMasMapper.getMyTicket(createBy);
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
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

    @Override
    public void createTicketRemarks(Integer ticketMasId, String remarksType, String remarks) {
        String createby = userService.getCurrentUserUserId();
        ticketRemarkMapper.insertTicketRemarks(ticketMasId, remarksType, remarks, createby);
    }

    @Override
    public void updateJobIdInService(Integer jobId, String ticketMasId, String userId) {
        ticketServiceMapper.updateTicketServiceByJobId(jobId, ticketMasId, userId);
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

        List<SdSymptomBean> beanList = symptomEntities.stream().map(entity -> {
            SdSymptomBean bean = new SdSymptomBean();
            ticketServiceBeanPopulator.populate(entity, bean);
            return bean;
        }).collect(Collectors.toList());

        return beanList;
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
        int ticketDetId = entity.getTicketDetId();
        return ticketDetId;
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
}
