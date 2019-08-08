package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

import java.time.LocalDateTime;

public class SdAccessRequestVisitorEntity extends BaseEntity {
    private Integer visitorAccessId;
    private Integer accessRequestId;
    private Integer hashedRequestId;

    private String visitorName;
    private String companyName;
    private byte[] staffId;
    private byte[] mobile;

    private LocalDateTime timeIn;
    private LocalDateTime timeOut;
    private String visitorCardNum;


    private LocalDateTime visitDateFrom;
    private String visitLocation;


    public Integer getVisitorAccessId() {
        return visitorAccessId;
    }

    public void setVisitorAccessId(Integer visitorAccessId) {
        this.visitorAccessId = visitorAccessId;
    }

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

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public byte[] getStaffId() {
        return staffId;
    }

    public void setStaffId(byte[] staffId) {
        this.staffId = staffId;
    }

    public byte[] getMobile() {
        return mobile;
    }

    public void setMobile(byte[] mobile) {
        this.mobile = mobile;
    }

    public LocalDateTime getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(LocalDateTime timeIn) {
        this.timeIn = timeIn;
    }

    public LocalDateTime getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(LocalDateTime timeOut) {
        this.timeOut = timeOut;
    }

    public String getVisitorCardNum() {
        return visitorCardNum;
    }

    public void setVisitorCardNum(String visitorCardNum) {
        this.visitorCardNum = visitorCardNum;
    }

    public LocalDateTime getVisitDateFrom() {
        return visitDateFrom;
    }

    public void setVisitDateFrom(LocalDateTime visitDateFrom) {
        this.visitDateFrom = visitDateFrom;
    }

    public String getVisitLocation() {
        return visitLocation;
    }

    public void setVisitLocation(String visitLocation) {
        this.visitLocation = visitLocation;
    }
}
