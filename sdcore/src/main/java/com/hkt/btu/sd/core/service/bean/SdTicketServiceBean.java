package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

import java.util.List;

public class SdTicketServiceBean extends BaseBean {

    private int ticketDetId;
    private int ticketMasId;
    private String serviceTypeCode;
    private String serviceId;
    private List<SdServiceFaultsBean> faultsList;

    public int getTicketDetId() {
        return ticketDetId;
    }

    public void setTicketDetId(int ticketDetId) {
        this.ticketDetId = ticketDetId;
    }

    public int getTicketMasId() {
        return ticketMasId;
    }

    public void setTicketMasId(int ticketMasId) {
        this.ticketMasId = ticketMasId;
    }

    public String getServiceTypeCode() {
        return serviceTypeCode;
    }

    public void setServiceTypeCode(String serviceTypeCode) {
        this.serviceTypeCode = serviceTypeCode;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public List<SdServiceFaultsBean> getFaultsList() {
        return faultsList;
    }

    public void setFaultsList(List<SdServiceFaultsBean> faultsList) {
        this.faultsList = faultsList;
    }
}
