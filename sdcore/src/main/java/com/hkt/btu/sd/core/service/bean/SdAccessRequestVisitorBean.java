package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class SdAccessRequestVisitorBean extends BaseBean {
    private Integer visitorAccessId;
    private Integer accessRequestId;
    private Integer hashedRequestId;

    private String name;
    private String companyName;
    private String staffId;
    private String mobile;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeIn;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeOut;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public LocalTime getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(LocalTime timeOut) {
        this.timeOut = timeOut;
    }

    public LocalTime getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(LocalTime timeIn) {
        this.timeIn = timeIn;
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
