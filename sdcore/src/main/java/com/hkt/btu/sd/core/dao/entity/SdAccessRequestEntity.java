package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

import java.time.LocalDateTime;


public class SdAccessRequestEntity extends BaseEntity {

    public class STATUS {
        public static final String APPROVED = "A";
        public static final String COMPLETED = "C";
    }

    public class LOC {
        public static final String JBY = "JBY";
        public static final String JBY_NAME = "JBY - Tseung Kwan O";
        public static final String LKT_2F = "LKT2F";
        public static final String LKT_2F_NAME = "LKT 2F - Wan Chai";
        public static final String LKT_5F = "LKT5F";
        public static final String LKT_5F_NAME = "LKT 5F - Wan Chai";
        public static final String VTA = "VTA";
        public static final String VTA_NAME = "VTA - Sheung Wan";
        public static final String MCX_3F = "MCX3F";
        public static final String MCX_3F_NAME = "MCX 3F - Kwai Chung";
        public static final String MCX_6F = "MCX6F";
        public static final String MCX_6F_NAME = "MCX 6F - Kwai Chung";
        public static final String IAC = "IAC";
        public static final String IAC_NAME = "IAC - Tseung Kwan O Industrial Estate";
    }

    private Integer accessRequestId;
    private Integer hashedRequestId;
    private String status;

    private Integer requesterId;
    private String requesterName;
    private byte[] mobile;
    private byte[] email;
    private Integer companyId;
    private String companyName;

    private String visitReason;
    private String visitLocation;
    private String visitRackNum;
    private LocalDateTime visitDateFrom;
    private LocalDateTime visitDateTo;

    private Integer visitorCount;


    public Integer getAccessRequestId() {
        return accessRequestId;
    }

    public void setAccessRequestId(Integer accessRequestId) {
        this.accessRequestId = accessRequestId;
    }

    public Integer getHashedRequestId() {
        return hashedRequestId;
    }

    public void setHashedRequestId(Integer hashedRequestId) {
        this.hashedRequestId = hashedRequestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Integer requesterId) {
        this.requesterId = requesterId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public byte[] getMobile() {
        return mobile;
    }

    public void setMobile(byte[] mobile) {
        this.mobile = mobile;
    }

    public byte[] getEmail() {
        return email;
    }

    public void setEmail(byte[] email) {
        this.email = email;
    }

    public String getVisitReason() {
        return visitReason;
    }

    public void setVisitReason(String visitReason) {
        this.visitReason = visitReason;
    }

    public String getVisitLocation() {
        return visitLocation;
    }

    public void setVisitLocation(String visitLocation) {
        this.visitLocation = visitLocation;
    }

    public String getVisitRackNum() {
        return visitRackNum;
    }

    public void setVisitRackNum(String visitRackNum) {
        this.visitRackNum = visitRackNum;
    }

    public LocalDateTime getVisitDateFrom() {
        return visitDateFrom;
    }

    public void setVisitDateFrom(LocalDateTime visitDateFrom) {
        this.visitDateFrom = visitDateFrom;
    }

    public LocalDateTime getVisitDateTo() {
        return visitDateTo;
    }

    public void setVisitDateTo(LocalDateTime visitDateTo) {
        this.visitDateTo = visitDateTo;
    }

    public Integer getVisitorCount() {
        return visitorCount;
    }

    public void setVisitorCount(Integer visitorCount) {
        this.visitorCount = visitorCount;
    }
}
