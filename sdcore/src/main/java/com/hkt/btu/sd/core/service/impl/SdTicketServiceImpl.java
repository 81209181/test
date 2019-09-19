package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.sd.core.dao.entity.SdTicketMasEntity;
import com.hkt.btu.sd.core.dao.entity.SdTicketRemarkEntity;
import com.hkt.btu.sd.core.dao.entity.SdTicketServiceEntity;
import com.hkt.btu.sd.core.dao.mapper.SdTicketContactMapper;
import com.hkt.btu.sd.core.dao.mapper.SdTicketMasMapper;
import com.hkt.btu.sd.core.dao.mapper.SdTicketRemarkMapper;
import com.hkt.btu.sd.core.dao.mapper.SdTicketServiceMapper;
import com.hkt.btu.sd.core.service.SdTicketService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdTicketContactBean;
import com.hkt.btu.sd.core.service.bean.SdTicketMasBean;
import com.hkt.btu.sd.core.service.bean.SdTicketRemarkBean;
import com.hkt.btu.sd.core.service.bean.SdTicketServiceBean;
import com.hkt.btu.sd.core.service.populator.SdTicketContactBeanPopulator;
import com.hkt.btu.sd.core.service.populator.SdTicketMasBeanPopulator;
import com.hkt.btu.sd.core.service.populator.SdTicketRemarkBeanPopulator;
import com.hkt.btu.sd.core.service.populator.SdTicketServiceBeanPopulator;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
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

    @Resource(name = "ticketMasBeanPopulator")
    SdTicketMasBeanPopulator ticketMasBeanPopulator;
    @Resource(name = "ticketContactBeanPopulator")
    SdTicketContactBeanPopulator ticketContactBeanPopulator;
    @Resource(name = "ticketServiceBeanPopulator")
    SdTicketServiceBeanPopulator ticketServiceBeanPopulator;
    @Resource(name = "ticketRemarkBeanPopulator")
    SdTicketRemarkBeanPopulator ticketRemarkBeanPopulator;

    @Override
    public Optional<SdTicketMasBean> createQueryTicket(String custCode) {
        SdTicketMasBean bean = new SdTicketMasBean();
        SdTicketMasEntity ticketMasEntity = new SdTicketMasEntity();
        ticketMasEntity.setCustCode(custCode);
        ticketMasEntity.setCreateby(userService.getCurrentUserUserId());
        ticketMasMapper.insertQueryTicket(ticketMasEntity);
        ticketMasBeanPopulator.populate(ticketMasEntity, bean);
        return Optional.of(bean);
    }

    @Override
    public Optional<SdTicketMasBean> getTicket(Integer ticketId) {
        SdTicketMasEntity ticketMasEntity = ticketMasMapper.findTicketById(ticketId);
        if (!ObjectUtils.isEmpty(ticketMasEntity)) {
            SdTicketMasBean bean = new SdTicketMasBean();
            ticketMasBeanPopulator.populate(ticketMasEntity, bean);
            return Optional.of(bean);
        }
        return Optional.empty();
    }

    @Override
    public void updateContactInfo(Integer ticketMasId, String contactType, String contactName, String contactNumber, String contactEmail, String contactMobile) {
        ticketContactMapper.insertTicketContactInfo(ticketMasId, contactType, contactName, contactMobile, contactEmail, contactNumber, userService.getCurrentUserUserId());
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
        List<SdTicketMasEntity> entityList = ticketMasMapper.getMyTicket(userService.getCurrentUserUserId());
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
    public List<SdTicketRemarkBean> getRemarkInfo(Integer ticketMasId) {
        List<SdTicketRemarkBean> beanList = new ArrayList<>();
        ticketRemarkMapper.getTicketRemarksByTicketId(ticketMasId).forEach(sdTicketRemarkEntity -> {
            SdTicketRemarkBean bean = new SdTicketRemarkBean();
            ticketRemarkBeanPopulator.populate(sdTicketRemarkEntity,bean);
            beanList.add(bean);
        });
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
    public void removeServiceInfoByTicketMasId(Integer ticketMasId) {
        ticketServiceMapper.removeServiceInfoByTicketMasId(ticketMasId);
        List<SdTicketServiceEntity> serviceInfoList = ticketServiceMapper.getTicketServiceInfoByTicketMasId(ticketMasId);
        if (CollectionUtils.isNotEmpty(serviceInfoList)) {
            List<Integer> ticketServiceIds = serviceInfoList.stream().map(SdTicketServiceEntity::getTicketDetId).collect(Collectors.toList());
            ticketServiceIds.forEach(ticketDetId -> ticketServiceMapper.removeFaultsByTicketDetId(ticketDetId));
        }
    }

    @Override
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
    public void updateFaultsInfo(Integer ticketDetId, String faults) {
        BtuUserBean currentUserBean = userService.getCurrentUserBean();
        String createBy = currentUserBean.getUserId();

        ticketServiceMapper.insertFaults(ticketDetId, faults, createBy, createBy);
    }

    @Override
    public void updateRemark(Integer ticketMasId, String remarksType, String remarks) {
        if (!remarksType.equals(SdTicketRemarkEntity.REMARKS_TYPE.SYSTEM)) {
            ticketRemarkMapper.insertTicketRemarks(ticketMasId,remarksType,remarks,userService.getCurrentUserUserId());
        }
    }

    @Override
    public void removeRemarkByTicketMasId(Integer ticketMasId) {
        ticketRemarkMapper.removeTicketRemarks(ticketMasId);
    }
}
