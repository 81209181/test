package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.core.exception.AuthorityNotFoundException;
import com.hkt.btu.sd.core.service.SdTicketService;
import com.hkt.btu.sd.core.service.bean.SdServiceFaultsBean;
import com.hkt.btu.sd.core.service.bean.SdTicketContactBean;
import com.hkt.btu.sd.core.service.bean.SdTicketMasBean;
import com.hkt.btu.sd.core.service.bean.SdTicketServiceBean;
import com.hkt.btu.sd.facade.SdTicketFacade;
import com.hkt.btu.sd.facade.data.RequestTicketServiceData;
import com.hkt.btu.sd.facade.data.SdTicketContactData;
import com.hkt.btu.sd.facade.data.SdTicketMasData;
import com.hkt.btu.sd.facade.data.SdTicketServiceData;
import com.hkt.btu.sd.facade.populator.SdTicketContactDataPopulator;
import com.hkt.btu.sd.facade.populator.SdTicketMasDataPopulator;
import com.hkt.btu.sd.facade.populator.SdTicketServiceDataPopulator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SdTicketFacadeImpl implements SdTicketFacade {

    private static final Logger LOG = LogManager.getLogger(SdTicketFacadeImpl.class);

    @Resource(name = "ticketService")
    SdTicketService ticketService;

    @Resource(name = "ticketMasDataPopulator")
    SdTicketMasDataPopulator ticketMasDataPopulator;
    @Resource(name = "ticketContactDataPopulator")
    SdTicketContactDataPopulator ticketContactDataPopulator;
    @Resource(name = "ticketServiceDataPopulator")
    SdTicketServiceDataPopulator ticketServiceDataPopulator;

    @Override
    public Optional<SdTicketMasData> createQueryTicket(String custCode) {
        Optional<SdTicketMasBean> bean = ticketService.createQueryTicket(custCode);
        if (bean.isPresent()) {
            SdTicketMasData ticketMasData = new SdTicketMasData();
            ticketMasDataPopulator.populate(bean.get(), ticketMasData);
            return Optional.of(ticketMasData);
        }
        return Optional.empty();
    }

    @Override
    public Optional<SdTicketMasData> getTicket(Integer ticketId) {
        Optional<SdTicketMasBean> bean = ticketService.getTicket(ticketId);
        if (bean.isPresent()) {
            SdTicketMasData ticketMasData = new SdTicketMasData();
            ticketMasDataPopulator.populate(bean.get(), ticketMasData);
            return Optional.of(ticketMasData);
        }
        return Optional.empty();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateContactInfo(List<SdTicketContactData> contactList) {
        ticketService.removeContactInfoByTicketMasId(contactList.get(0).getTicketMasId());
        contactList.forEach(data -> {
            ticketService.updateContactInfo(data.getTicketMasId(), data.getContactTypeValue(), data.getContactName(), data.getContactNumber(), data.getContactEmail(), data.getContactMobile());
        });
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
    public PageData<SdTicketMasData> searchTicketList(Pageable pageable, String dateFrom, String dateTo, String status) {
        Page<SdTicketMasBean> pageBean;
        try {
            dateFrom = StringUtils.isEmpty(dateFrom) ? null : dateFrom;
            dateTo = StringUtils.isEmpty(dateTo) ? null : dateTo;
            status = StringUtils.isEmpty(status) ? null : status;

            pageBean = ticketService.searchTicketList(pageable, dateFrom, dateTo, status);
        } catch (AuthorityNotFoundException e) {
            return new PageData<>(e.getMessage());
        }

        // populate content
        List<SdTicketMasBean> beanList = pageBean.getContent();
        List<SdTicketMasData> dataList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(beanList)) {
            for (SdTicketMasBean bean : beanList) {
                SdTicketMasData data = new SdTicketMasData();
                ticketMasDataPopulator.populate(bean, data);
                dataList.add(data);
            }
        }

        return new PageData<>(dataList, pageBean.getPageable(), pageBean.getTotalElements());
    }

    @Override
    public List<SdTicketMasData> getMyTicket() {
        List<SdTicketMasBean> beanList = ticketService.getMyTicket();

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
    public List<SdTicketServiceData> getServiceInfo(Integer ticketMasId) {
        List<SdTicketServiceBean> serviceInfoList = ticketService.getServiceInfo(ticketMasId);
        if (CollectionUtils.isNotEmpty(serviceInfoList)) {
            return serviceInfoList.stream().map(bean -> {
                SdTicketServiceData data = new SdTicketServiceData();
                ticketServiceDataPopulator.populate(bean, data);
                return data;
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public String updateServiceInfo(List<RequestTicketServiceData> serviceList) {
        Integer ticketMasId = serviceList.get(0).getTicketMasId();
        try {
            ticketService.removeServiceInfoByTicketMasId(ticketMasId);

            List<SdTicketServiceBean> serviceInfoList = serviceList.stream().map(requestData -> {
                SdTicketServiceBean bean = new SdTicketServiceBean();
                bean.setTicketMasId(requestData.getTicketMasId());
                bean.setServiceTypeCode(requestData.getServiceType());
                bean.setServiceId(requestData.getServiceCode());
                bean.setFaults(requestData.getFaults());
                return bean;
            }).collect(Collectors.toList());

            serviceInfoList.forEach(serviceInfo -> {
                int ticketDetId = ticketService.updateServiceInfo(serviceInfo);
                serviceInfo.getFaults().forEach(faults -> ticketService.updateFaultsInfo(ticketDetId, faults));
            });
        } catch (Exception e) {
            LOG.error(e);
            return "update service info failed.";
        }
        return null;
    }
}
