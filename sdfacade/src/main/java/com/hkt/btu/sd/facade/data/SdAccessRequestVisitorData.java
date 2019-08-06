package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class SdAccessRequestVisitorData implements DataInterface {
    private Integer visitorAccessId;
    private String hashedRequestId;

    private String name;
    private String company;
    private String staffId;
    private String mobile;

    private boolean canCheckInOut;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeIn;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeOut;

    private String visitorCardNum;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate visitDate;
    private String visitLocation;



    public Integer getVisitorAccessId() {
        return visitorAccessId;
    }

    public void setVisitorAccessId(Integer visitorAccessId) {
        this.visitorAccessId = visitorAccessId;
    }

    public String getHashedRequestId() {
        return hashedRequestId;
    }

    public void setHashedRequestId(String hashedRequestId) {
        this.hashedRequestId = hashedRequestId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public boolean isCanCheckInOut() {
        return canCheckInOut;
    }

    public void setCanCheckInOut(boolean canCheckInOut) {
        this.canCheckInOut = canCheckInOut;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public String getVisitLocation() {
        return visitLocation;
    }

    public void setVisitLocation(String visitLocation) {
        this.visitLocation = visitLocation;
    }
}
