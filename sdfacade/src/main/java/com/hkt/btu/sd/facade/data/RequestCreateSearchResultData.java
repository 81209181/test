package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class RequestCreateSearchResultData implements DataInterface {

    private String adminId;
    private long custId;
    private String customer;
    private String email;
    private String mobileNo;
    private String url;
    private String contactName;
    private String contactEmail;
    private String contactNumber;
    private String jmsOrderNum;
    private String l1OrderNum;
    private String lastUpdId;
    private int packageType;
    private String password;
    private int pid;
    private String prefix;
    private String productDesc;
    private int profileId;
    private String resourceId;
    private String resourceName;
    private String status;
    private String statusDesc;
    private String stb;
    private int type;
    private String userName;
    private int tenantId;
    private String domainName;

    // customer info
    private String custCode;
    private String custName;
    private String custType;
    private String custStatus;
    private String languagePreference;

    // service info
    private String serviceType;
    private String serviceNo;
    private Long subsId;
    private String offerName;
    private String serviceStatus;


    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
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

    public Long getSubsId() {
        return subsId;
    }

    public void setSubsId(Long subsId) {
        this.subsId = subsId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public long getCustId() {
        return custId;
    }

    public void setCustId(long custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getJmsOrderNum() {
        return jmsOrderNum;
    }

    public void setJmsOrderNum(String jmsOrderNum) {
        this.jmsOrderNum = jmsOrderNum;
    }

    public String getL1OrderNum() {
        return l1OrderNum;
    }

    public void setL1OrderNum(String l1OrderNum) {
        this.l1OrderNum = l1OrderNum;
    }

    public String getLastUpdId() {
        return lastUpdId;
    }

    public void setLastUpdId(String lastUpdId) {
        this.lastUpdId = lastUpdId;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public int getPackageType() {
        return packageType;
    }

    public void setPackageType(int packageType) {
        this.packageType = packageType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getStb() {
        return stb;
    }

    public void setStb(String stb) {
        this.stb = stb;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public int getType() {
        return type;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
