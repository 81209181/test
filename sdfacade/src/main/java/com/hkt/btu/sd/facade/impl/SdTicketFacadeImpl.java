package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.core.exception.AuthorityNotFoundException;
import com.hkt.btu.sd.core.service.SdTicketService;
import com.hkt.btu.sd.core.service.bean.SdTicketContactBean;
import com.hkt.btu.sd.core.service.bean.SdTicketMasBean;
import com.hkt.btu.sd.core.service.bean.SdTicketRemarkBean;
import com.hkt.btu.sd.core.service.bean.SdTicketServiceBean;
import com.hkt.btu.sd.facade.SdTicketFacade;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.populator.SdTicketContactDataPopulator;
import com.hkt.btu.sd.facade.populator.SdTicketMasDataPopulator;
import com.hkt.btu.sd.facade.populator.SdTicketRemarkDataPopulator;
import com.hkt.btu.sd.facade.populator.SdTicketServiceDataPopulator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
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
    @Resource(name = "ticketRemarkDataPopulator")
    SdTicketRemarkDataPopulator ticketRemarkDataPopulator;

    @Override
    public int createQueryTicket(String custCode, String serviceNo, String serviceType, String subsId) {
        return ticketService.createQueryTicket(custCode, serviceNo, serviceType, subsId);
    }

    @Override
    public Optional<SdTicketMasData> getTicket(Integer ticketId) {
        SdTicketMasData ticketMasData = new SdTicketMasData();
        return ticketService.getTicket(ticketId).map(sdTicketMasBean -> {
            ticketMasDataPopulator.populate(sdTicketMasBean, ticketMasData);
            return ticketMasData;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateContactInfo(List<SdTicketContactData> contactList) {

        for (SdTicketContactData data : contactList) {
            if (StringUtils.isEmpty(data.getContactName())) {
                return "Contact name is empty.";
            } else if (StringUtils.isEmpty(data.getContactNumber()) && StringUtils.isEmpty(data.getContactMobile()) && StringUtils.isEmpty(data.getContactEmail())) {
                return "Contact No.,Contact Mobile,Contact Email must have one not empty.";
            }
        }

        try {
            ticketService.removeContactInfoByTicketMasId(contactList.get(0).getTicketMasId());
            contactList.forEach(data -> {
                ticketService.insertTicketContactInfo(data.getTicketMasId(), data.getContactTypeValue(), data.getContactName(), data.getContactNumber(), data.getContactEmail(), data.getContactMobile());
            });
        } catch (Exception e){
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
    public List<SdTicketRemarkData> getTicketRemarksByTicketId(Integer ticketMasId) {
        List<SdTicketRemarkBean> beanList = ticketService.getTicketRemarksByTicketId(ticketMasId);
        if (CollectionUtils.isEmpty(beanList)) {
            return null;
        }

        List<SdTicketRemarkData> dataList = new LinkedList<>();
        for (SdTicketRemarkBean bean : beanList) {
            SdTicketRemarkData data = new SdTicketRemarkData();
            ticketRemarkDataPopulator.populate(bean, data);
            dataList.add(data);
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

    @Override
    public String createTicketRemarks(Integer ticketMasId, String remarksType, String remarks) {
        if (ticketMasId == null) {
            return "Ticket Mas ID is empty.";
        } else if (StringUtils.isEmpty(remarksType)) {
            return "Remarks Type is empty.";
        } else if (StringUtils.isEmpty(remarks)) {
            return "Remarks is empty.";
        }

        try {
            ticketService.createTicketRemarks(ticketMasId, remarksType, remarks);
        } catch (DuplicateKeyException e){
            return "Duplicate data already exists.";
        }

        return null;
    }

    @Override
    public void updateJobIdInService(Integer jobId, String ticketMasId, String userId) {
        ticketService.updateJobIdInService(jobId, ticketMasId, userId);
    }

    @Override
    public Optional<SdTicketServiceData> getService(Integer ticketId) {
        return ticketService.getService(ticketId).map(sdTicketServiceBean -> {
            SdTicketServiceData data = new SdTicketServiceData();
            ticketServiceDataPopulator.populate(sdTicketServiceBean, data);
            return data;
        });
    }

    @Override
    public void updateAppointment(String appointmentDate, boolean asap, String userId, String ticketMasId) {
        ticketService.updateAppointment(appointmentDate, asap, userId,ticketMasId);
    }

    @Override
    public boolean checkAppointmentDate(String appointmentDate) {
        return ticketService.checkAppointmentDate(appointmentDate);
    }
}
