package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

public class SdTicketContactEntity extends BaseEntity {

    private Integer ticketContactId;

    private Integer ticketMasId;

    private String contactType;

    private String contactName;

    private String contactNumber;

    private String contactMobile;

    private String contactEmail;

    public String getContactTypeDesc(){
        switch (contactType) {
            case "CUST":
                return "Customer";
            case "ADMIN":
                return "Office Admin";
            case "SITE":
                return "On-site Contact";
            case "CALL":
                return "Caller";
        }
        return "Not found match contact type";
    }

    public Integer getTicketContactId() {
        return ticketContactId;
    }

    public void setTicketContactId(Integer ticketContactId) {
        this.ticketContactId = ticketContactId;
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
        this.contactType = contactType;
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
