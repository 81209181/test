package com.hkt.btu.noc.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

import java.time.LocalDateTime;
import java.util.List;

public class NocAccessRequestBean extends BaseBean {
    private Integer accessRequestId;
    private Integer hashedRequestId;
    private String status;

    private NocUserBean requester;
    private NocCompanyBean requesterCompany;

    private String visitReason;
    private String visitLocation;
    private String visitRackNum;
    private LocalDateTime visitDateFrom;
    private LocalDateTime visitDateTo;


    private List<NocAccessRequestVisitorBean> requestVisitorBeanList;
    private Integer visitorCount;

    private List<NocAccessRequestEquipBean> requestEquipBeanList;


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

    public NocUserBean getRequester() {
        return requester;
    }

    public void setRequester(NocUserBean requester) {
        this.requester = requester;
    }

    public NocCompanyBean getRequesterCompany() {
        return requesterCompany;
    }

    public void setRequesterCompany(NocCompanyBean requesterCompany) {
        this.requesterCompany = requesterCompany;
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

    public List<NocAccessRequestVisitorBean> getRequestVisitorBeanList() {
        return requestVisitorBeanList;
    }

    public void setRequestVisitorBeanList(List<NocAccessRequestVisitorBean> requestVisitorBeanList) {
        this.requestVisitorBeanList = requestVisitorBeanList;
    }

    public Integer getVisitorCount() {
        return visitorCount;
    }

    public void setVisitorCount(Integer visitorCount) {
        this.visitorCount = visitorCount;
    }

    public List<NocAccessRequestEquipBean> getRequestEquipBeanList() {
        return requestEquipBeanList;
    }

    public void setRequestEquipBeanList(List<NocAccessRequestEquipBean> requestEquipBeanList) {
        this.requestEquipBeanList = requestEquipBeanList;
    }
}
