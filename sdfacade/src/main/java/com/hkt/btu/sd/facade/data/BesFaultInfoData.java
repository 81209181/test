package com.hkt.btu.sd.facade.data;

public class BesFaultInfoData {

    private String subscriberName;
    private long requestId;
    private String custName;
    private String productType;
    private Integer repeatedGroupIdCount;
    private String faultId;
    private String mainFaultCode;
    private Integer repeatedSubscriberIdCount;
    private String subFaultId;
    private String subFaultCode;
    private String symptom;
    private String subFaultStatus;
    private String createdBy;
    private String createdDate;
    private String lastUpdatedDate;
    private String lastUpdatedBy;
    private String closedDate;

    public String getSubscriberName() {
        return subscriberName;
    }

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getRepeatedGroupIdCount() {
        return repeatedGroupIdCount;
    }

    public void setRepeatedGroupIdCount(Integer repeatedGroupIdCount) {
        this.repeatedGroupIdCount = repeatedGroupIdCount;
    }

    public String getFaultId() {
        return faultId;
    }

    public void setFaultId(String faultId) {
        this.faultId = faultId;
    }

    public String getMainFaultCode() {
        return mainFaultCode;
    }

    public void setMainFaultCode(String mainFaultCode) {
        this.mainFaultCode = mainFaultCode;
    }

    public Integer getRepeatedSubscriberIdCount() {
        return repeatedSubscriberIdCount;
    }

    public void setRepeatedSubscriberIdCount(Integer repeatedSubscriberIdCount) {
        this.repeatedSubscriberIdCount = repeatedSubscriberIdCount;
    }

    public String getSubFaultId() {
        return subFaultId;
    }

    public void setSubFaultId(String subFaultId) {
        this.subFaultId = subFaultId;
    }

    public String getSubFaultCode() {
        return subFaultCode;
    }

    public void setSubFaultCode(String subFaultCode) {
        this.subFaultCode = subFaultCode;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getSubFaultStatus() {
        return subFaultStatus;
    }

    public void setSubFaultStatus(String subFaultStatus) {
        this.subFaultStatus = subFaultStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate;
    }
}
