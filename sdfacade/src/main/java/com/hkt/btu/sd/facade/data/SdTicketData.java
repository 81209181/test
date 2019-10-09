package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

public class SdTicketData implements DataInterface {

    private SdTicketMasData ticketMasInfo;
    private List<SdTicketContactData> contactInfo;
    private List<SdTicketServiceData> serviceInfo;
    private List<SdTicketRemarkData> remarkInfo;

    public SdTicketMasData getTicketMasInfo() {
        return ticketMasInfo;
    }

    public void setTicketMasInfo(SdTicketMasData ticketMasInfo) {
        this.ticketMasInfo = ticketMasInfo;
    }

    public List<SdTicketContactData> getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(List<SdTicketContactData> contactInfo) {
        this.contactInfo = contactInfo;
    }

    public List<SdTicketServiceData> getServiceInfo() {
        return serviceInfo;
    }

    public void setServiceInfo(List<SdTicketServiceData> serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    public List<SdTicketRemarkData> getRemarkInfo() {
        return remarkInfo;
    }

    public void setRemarkInfo(List<SdTicketRemarkData> remarkInfo) {
        this.remarkInfo = remarkInfo;
    }
}
