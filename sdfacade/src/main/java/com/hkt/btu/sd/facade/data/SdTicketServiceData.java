package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

public class SdTicketServiceData implements DataInterface {

    private Integer ticketDetId;
    private String serviceType;
    private String serviceCode;
    private List<SdServiceFaultsData> faultsList;

    public Integer getTicketDetId() {
        return ticketDetId;
    }

    public void setTicketDetId(Integer ticketDetId) {
        this.ticketDetId = ticketDetId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public List<SdServiceFaultsData> getFaultsList() {
        return faultsList;
    }

    public void setFaultsList(List<SdServiceFaultsData> faultsList) {
        this.faultsList = faultsList;
    }
}
