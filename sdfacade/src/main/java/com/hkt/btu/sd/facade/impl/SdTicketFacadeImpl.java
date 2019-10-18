package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.core.exception.AuthorityNotFoundException;
import com.hkt.btu.sd.core.service.SdTicketService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.*;
import com.hkt.btu.sd.facade.SdAuditTrailFacade;
import com.hkt.btu.sd.facade.SdRequestCreateFacade;
import com.hkt.btu.sd.facade.SdTicketFacade;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.populator.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

public class SdTicketFacadeImpl implements SdTicketFacade {

    private static final Logger LOG = LogManager.getLogger(SdTicketFacadeImpl.class);

    @Resource(name = "ticketService")
    SdTicketService ticketService;
    @Resource(name = "requestCreateFacade")
    SdRequestCreateFacade requestCreateFacade;
    @Resource(name = "auditTrailFacade")
    SdAuditTrailFacade auditTrailFacade;
    @Resource(name = "userService")
    SdUserService userService;

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

    @Override
    public int createQueryTicket(String custCode, String serviceNo, String serviceType, String subsId) {
        return ticketService.createQueryTicket(custCode, serviceNo, serviceType, subsId);
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
    public PageData<SdTicketMasData> searchTicketList(Pageable pageable, String dateFrom, String dateTo, String status, String ticketMasId, String custCode) {
        Page<SdTicketMasBean> pageBean;
        try {
            dateFrom = StringUtils.isEmpty(dateFrom) ? null : dateFrom;
            dateTo = StringUtils.isEmpty(dateTo) ? null : dateTo;
            status = StringUtils.isEmpty(status) ? null : status;
            ticketMasId = StringUtils.isEmpty(ticketMasId) ? null : ticketMasId;
            custCode = StringUtils.isEmpty(custCode) ? null : custCode;

            pageBean = ticketService.searchTicketList(pageable, dateFrom, dateTo, status, ticketMasId, custCode);
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
            ticketService.createTicketRemarks(ticketMasId, SdTicketRemarkData.Type.CUSTOMER, remarks);
        } catch (DuplicateKeyException e) {
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
        ticketService.updateAppointment(appointmentDate, asap, userId, ticketMasId);
    }

    @Override
    public boolean checkAppointmentDate(String appointmentDate) {
        return ticketService.checkAppointmentDate(appointmentDate);
    }

    @Override
    public List<SdSymptomData> getSymptom(Integer ticketMasId) {
        List<SdSymptomBean> beanList = ticketService.getSymptomList(ticketMasId);

        if (CollectionUtils.isEmpty(beanList)) {
            return null;
        }

        List<SdSymptomData> dataList = beanList.stream().map(bean -> {
            SdSymptomData data = new SdSymptomData();
            ticketServiceDataPopulator.populate(bean, data);
            return data;
        }).collect(Collectors.toList());

        return dataList;
    }

    @Override
    public BesSubFaultData getFaultInfo(String subscriberId) {
        if (StringUtils.isEmpty(subscriberId)) {
            return BesSubFaultData.MISSING_PARAM;
        }

        try {
            // get ticket det list
            List<SdTicketServiceBean> sdTicketServiceBeanList = ticketService.findServiceBySubscriberId(subscriberId);
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

            BesSubFaultData besSubFaultData = new BesSubFaultData();
            besSubFaultData.setList(besFaultInfoDataList);
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

    public SdTicketMasData getTicketMas(Integer ticketMasId) {
        SdTicketMasData sdticketMasData = new SdTicketMasData();
        Optional<SdTicketMasBean> sdTicketMasBean = ticketService.getTicket(ticketMasId);
        if (sdTicketMasBean.isPresent()) {
            ticketMasDataPopulator.populate(sdTicketMasBean.get(), sdticketMasData);
        }
        return sdticketMasData;
    }

    @Override
    public void cancelTicket(int ticketMasId, String userId) {
        ticketService.updateTicketStatus(ticketMasId, SdTicketMasBean.STATUS_TYPE_CODE.CANCEL, userId);
    }

    @Override
    public boolean isCancel(String ticketMasId) {
        return ticketService.getTicket(Integer.valueOf(ticketMasId))
                .map(SdTicketMasBean::getStatus)
                .filter(s -> s.equals(SdTicketMasBean.STATUS_TYPE.CANCEL)).isPresent();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseCallInCount(Integer ticketMasId) {
        if (ticketMasId == null) {
            return false;
        }

        try {
            ticketService.increaseCallInCount(ticketMasId);
            ticketService.createTicketRemarks(ticketMasId, SdTicketRemarkData.Type.SYSTEM, "Call In Count has been increase 1.");
            return true;
        } catch (Exception e) {
            LOG.warn(e);
            return false;
        }
    }
}
