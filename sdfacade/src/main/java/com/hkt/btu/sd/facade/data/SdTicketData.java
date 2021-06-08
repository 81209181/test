package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;
import com.hkt.btu.sd.facade.data.gmb.Parameter;

import java.util.List;
import java.util.Map;

public class SdTicketData implements DataInterface {

    private SdTicketMasData ticketMasInfo;
    private List<SdTicketContactData> contactInfo;
    private List<SdTicketServiceData> serviceInfo;
    private List<SdTicketRemarkData> remarkInfo;
    private List<Map<String, Object>> closeInfo;
    private List<Parameter> parameterList;

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

    public List<Map<String, Object>> getCloseInfo() {
        return closeInfo;
    }

    public void setCloseInfo(List<Map<String, Object>> closeInfo) {
        this.closeInfo = closeInfo;
    }

    public List<Parameter> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<Parameter> parameterList) {
        this.parameterList = parameterList;
    }
}
