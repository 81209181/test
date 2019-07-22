package com.hkt.btu.noc.facade.data;


import com.hkt.btu.common.facade.data.DataInterface;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CreateAccessRequestFormData implements DataInterface {

    private String requesterName;
    private String companyName;
    private String mobile;
    private String email;

    private String visitReason;
    private String visitLocation;
    private String visitRackNum;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate visitDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime visitTimeFrom;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime visitTimeTo;


    private List<NocAccessRequestVisitorData> visitorDataList;
    private List<NocAccessRequestEquipData> equipDataList;


    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVisitReason() {
        return visitReason;
    }

    public void setVisitReason(String visitReason) {
        this.visitReason = visitReason;
    }

    public List<NocAccessRequestVisitorData> getVisitorDataList() {
        return visitorDataList;
    }

    public void setVisitorDataList(List<NocAccessRequestVisitorData> visitorDataList) {
        this.visitorDataList = visitorDataList;
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

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public LocalTime getVisitTimeFrom() {
        return visitTimeFrom;
    }

    public void setVisitTimeFrom(LocalTime visitTimeFrom) {
        this.visitTimeFrom = visitTimeFrom;
    }

    public LocalTime getVisitTimeTo() {
        return visitTimeTo;
    }

    public void setVisitTimeTo(LocalTime visitTimeTo) {
        this.visitTimeTo = visitTimeTo;
    }

    public List<NocAccessRequestEquipData> getEquipDataList() {
        return equipDataList;
    }

    public void setEquipDataList(List<NocAccessRequestEquipData> equipDataList) {
        this.equipDataList = equipDataList;
    }
}
