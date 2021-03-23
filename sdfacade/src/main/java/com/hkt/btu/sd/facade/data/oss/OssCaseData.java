package com.hkt.btu.sd.facade.data.oss;

import com.hkt.btu.sd.facade.data.SdTicketContactData;
import com.hkt.btu.sd.facade.data.cloud.Attachment;
import com.hkt.btu.sd.facade.data.cloud.Attribute;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public class OssCaseData {

    private String serviceType;

    private String identityId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime triggerTime;

    private List<String> workingPartyList;

    private List<SdTicketContactData> contactInfo;

    private List<Attribute> attributes;

    private List<Attachment> attachments;

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public LocalDateTime getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(LocalDateTime triggerTime) {
        this.triggerTime = triggerTime;
    }

    public List<String> getWorkingPartyList() {
        return workingPartyList;
    }

    public void setWorkingPartyList(List<String> workingPartyList) {
        this.workingPartyList = workingPartyList;
    }

    public List<SdTicketContactData> getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(List<SdTicketContactData> contactInfo) {
        this.contactInfo = contactInfo;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
