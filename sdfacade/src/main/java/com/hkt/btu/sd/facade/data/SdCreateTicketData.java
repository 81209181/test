package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;


public class SdCreateTicketData implements DataInterface {

    private String serviceNumber;
    private String ticketType;
    private Integer priority;
    private List<SdTicketContactData> contactList;
    private String remarks;

    public String getServiceNumber() {
        return serviceNumber;
    }

    public void setServiceNumber(String serviceNumber) {
        this.serviceNumber = serviceNumber;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public List<SdTicketContactData> getContactList() {
        return contactList;
    }

    public void setContactList(List<SdTicketContactData> contactList) {
        this.contactList = contactList;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
