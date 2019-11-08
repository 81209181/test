package com.hkt.btu.sd.facade.data;

import java.time.LocalDateTime;

public class SdTicketInfoData {

    // ticket
    private int ticketMasId;
    private String ticketType;
    private String ticketStatus;
    private String ticketStatusDesc;
    private int callInCount;
    private String searchKeyDesc;
    private String searchValue;

    // customer info
    private String custCode;
    private String custName;
    private String custType;
    private String custStatus;
    private String languagePreference;
    private String asap;

    // job
    private String jobId;
    private String jobStatus;
    private LocalDateTime appointmentDate;

    // service info
    private String serviceType;
    private String serviceTypeDesc;
    private String serviceNo;
    private Long subsId;
    private String offerName;
    private String serviceStatus;
    private String serviceStatusDesc;
    private String serviceAddress;
    private String pendingOrder;
    private String supplementaryOffer;
    private String offerDetail;
    private String description;
    private String itsmUrl;
    private String gridId;
    private String exchangeBuildingId;
    private String relatedBsn;

    // other
    private boolean ngn3reset;

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

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getTicketStatusDesc() {
        return ticketStatusDesc;
    }

    public void setTicketStatusDesc(String ticketStatusDesc) {
        this.ticketStatusDesc = ticketStatusDesc;
    }

    public int getCallInCount() {
        return callInCount;
    }

    public void setCallInCount(int callInCount) {
        this.callInCount = callInCount;
    }

    public String getSearchKeyDesc() {
        return searchKeyDesc;
    }

    public void setSearchKeyDesc(String searchKeyDesc) {
        this.searchKeyDesc = searchKeyDesc;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getCustStatus() {
        return custStatus;
    }

    public void setCustStatus(String custStatus) {
        this.custStatus = custStatus;
    }

    public String getLanguagePreference() {
        return languagePreference;
    }

    public void setLanguagePreference(String languagePreference) {
        this.languagePreference = languagePreference;
    }

    public String getAsap() {
        return asap;
    }

    public void setAsap(String asap) {
        this.asap = asap;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceTypeDesc() {
        return serviceTypeDesc;
    }

    public void setServiceTypeDesc(String serviceTypeDesc) {
        this.serviceTypeDesc = serviceTypeDesc;
    }

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public Long getSubsId() {
        return subsId;
    }

    public void setSubsId(Long subsId) {
        this.subsId = subsId;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getServiceStatusDesc() {
        return serviceStatusDesc;
    }

    public void setServiceStatusDesc(String serviceStatusDesc) {
        this.serviceStatusDesc = serviceStatusDesc;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getPendingOrder() {
        return pendingOrder;
    }

    public void setPendingOrder(String pendingOrder) {
        this.pendingOrder = pendingOrder;
    }

    public String getSupplementaryOffer() {
        return supplementaryOffer;
    }

    public void setSupplementaryOffer(String supplementaryOffer) {
        this.supplementaryOffer = supplementaryOffer;
    }

    public String getOfferDetail() {
        return offerDetail;
    }

    public void setOfferDetail(String offerDetail) {
        this.offerDetail = offerDetail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItsmUrl() {
        return itsmUrl;
    }

    public void setItsmUrl(String itsmUrl) {
        this.itsmUrl = itsmUrl;
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public String getExchangeBuildingId() {
        return exchangeBuildingId;
    }

    public void setExchangeBuildingId(String exchangeBuildingId) {
        this.exchangeBuildingId = exchangeBuildingId;
    }

    public String getRelatedBsn() {
        return relatedBsn;
    }

    public void setRelatedBsn(String relatedBsn) {
        this.relatedBsn = relatedBsn;
    }

    public boolean isNgn3reset() {
        return ngn3reset;
    }

    public void setNgn3reset(boolean ngn3reset) {
        this.ngn3reset = ngn3reset;
    }
}
