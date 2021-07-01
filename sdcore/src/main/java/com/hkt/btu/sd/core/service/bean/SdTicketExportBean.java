package com.hkt.btu.sd.core.service.bean;

import java.time.LocalDateTime;

public class SdTicketExportBean {

    private int ticketMasId ;

    private String ticketType;

    private String status;

    private LocalDateTime completeDate;

    private int callInCount;

    private String serviceNumber;

    private String owningRole;

    private LocalDateTime createDate;

    private String createBy;

    private LocalDateTime modifyDate;

    private String modifyBy;

    private String symptomDescription;

    private String subsId;

    private String jobId;

    private LocalDateTime reportTime;

    private String sdCloseCodeDescription;

    private String wfmClearCode;

    private String wfmSubClearCode;

    public int getTicketMasId() {
        return ticketMasId;
    }

    public void setTicketMasId(int ticketMasId) {
        this.ticketMasId = ticketMasId;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(LocalDateTime completeDate) {
        this.completeDate = completeDate;
    }

    public int getCallInCount() {
        return callInCount;
    }

    public void setCallInCount(int callInCount) {
        this.callInCount = callInCount;
    }

    public String getServiceNumber() {
        return serviceNumber;
    }

    public void setServiceNumber(String serviceNumber) {
        this.serviceNumber = serviceNumber;
    }

    public String getOwningRole() {
        return owningRole;
    }

    public void setOwningRole(String owningRole) {
        this.owningRole = owningRole;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getSymptomDescription() {
        return symptomDescription;
    }

    public void setSymptomDescription(String symptomDescription) {
        this.symptomDescription = symptomDescription;
    }

    public String getSubsId() {
        return subsId;
    }

    public void setSubsId(String subsId) {
        this.subsId = subsId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public LocalDateTime getReportTime() {
        return reportTime;
    }

    public void setReportTime(LocalDateTime reportTime) {
        this.reportTime = reportTime;
    }

    public String getSdCloseCodeDescription() {
        return sdCloseCodeDescription;
    }

    public void setSdCloseCodeDescription(String sdCloseCodeDescription) {
        this.sdCloseCodeDescription = sdCloseCodeDescription;
    }

    public String getWfmClearCode() {
        return wfmClearCode;
    }

    public void setWfmClearCode(String wfmClearCode) {
        this.wfmClearCode = wfmClearCode;
    }

    public String getWfmSubClearCode() {
        return wfmSubClearCode;
    }

    public void setWfmSubClearCode(String wfmSubClearCode) {
        this.wfmSubClearCode = wfmSubClearCode;
    }
}
