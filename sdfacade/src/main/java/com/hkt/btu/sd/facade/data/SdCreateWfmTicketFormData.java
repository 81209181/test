package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;


public class SdCreateWfmTicketFormData implements DataInterface {

    private Integer ticketMasId;
    private List<SdTicketContactData> contactList;
    private List<SdRequestTicketServiceData> serviceList;
    private String remarks;

    public Integer getTicketMasId() {
        return ticketMasId;
    }

    public void setTicketMasId(Integer ticketMasId) {
        this.ticketMasId = ticketMasId;
    }

    public List<SdTicketContactData> getContactList() {
        return contactList;
    }

    public void setContactList(List<SdTicketContactData> contactList) {
        this.contactList = contactList;
    }

    public List<SdRequestTicketServiceData> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<SdRequestTicketServiceData> serviceList) {
        this.serviceList = serviceList;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
