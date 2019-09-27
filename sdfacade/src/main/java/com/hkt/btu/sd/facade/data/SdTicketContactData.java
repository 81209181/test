package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class SdTicketContactData implements DataInterface {

    private Integer ticketMasId;

    private String contactType;

    private String contactName;

    private String contactNumber;

    private String contactMobile;

    private String contactEmail;

    public String getContactTypeValue() {
        switch (contactType) {
            case "Customer":
                return "CUST";
            case "Office Admin":
                return "ADMIN";
            case "On-site Contact":
                return "SITE";
            case "Caller":
                return "CALL";
            default:
                return "Not found match contact type";
        }
    }

    public Integer getTicketMasId() {
        return ticketMasId;
    }

    public void setTicketMasId(Integer ticketMasId) {
        this.ticketMasId = ticketMasId;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType =contactType;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

}
