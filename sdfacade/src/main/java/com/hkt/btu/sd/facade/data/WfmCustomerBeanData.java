package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class WfmCustomerBeanData implements DataInterface {

    private String serviceId;
    private String mainFaultCode;
    private String testLineResult;
    private String faultId;
    private String offerName;
    private String domainName;
    private String tenantId;

    private WfmSfBeanListData[] sfBeanList;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getMainFaultCode() {
        return mainFaultCode;
    }

    public void setMainFaultCode(String mainFaultCode) {
        this.mainFaultCode = mainFaultCode;
    }

    public String getTestLineResult() {
        return testLineResult;
    }

    public void setTestLineResult(String testLineResult) {
        this.testLineResult = testLineResult;
    }

    public String getFaultId() {
        return faultId;
    }

    public void setFaultId(String faultId) {
        this.faultId = faultId;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public WfmSfBeanListData[] getSfBeanList() {
        return sfBeanList;
    }

    public void setSfBeanList(WfmSfBeanListData[] sfBeanList) {
        this.sfBeanList = sfBeanList;
    }
}
