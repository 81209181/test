package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class WfmRequestDetailsBeanDate implements DataInterface {

    private String cust_name;
    private String groupId;
    private String cust_id_type;
    private String cust_id_doc_name;
    private String cust_address;

    private Long requestId;

    private String cust_business_fax;
    private String cust_business_tel;
    private String productType;
    private String offerNames;
    private Boolean majorIncident;
    private String tenantIds;

    private WfmFdBeanListData[] fdBeanList;

    private WfmContactBeanData contactBean;
    private WfmCustomerBeanData customerBean;

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCust_id_type() {
        return cust_id_type;
    }

    public void setCust_id_type(String cust_id_type) {
        this.cust_id_type = cust_id_type;
    }

    public String getCust_id_doc_name() {
        return cust_id_doc_name;
    }

    public void setCust_id_doc_name(String cust_id_doc_name) {
        this.cust_id_doc_name = cust_id_doc_name;
    }

    public String getCust_address() {
        return cust_address;
    }

    public void setCust_address(String cust_address) {
        this.cust_address = cust_address;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getCust_business_fax() {
        return cust_business_fax;
    }

    public void setCust_business_fax(String cust_business_fax) {
        this.cust_business_fax = cust_business_fax;
    }

    public String getCust_business_tel() {
        return cust_business_tel;
    }

    public void setCust_business_tel(String cust_business_tel) {
        this.cust_business_tel = cust_business_tel;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getOfferNames() {
        return offerNames;
    }

    public void setOfferNames(String offerNames) {
        this.offerNames = offerNames;
    }

    public Boolean getMajorIncident() {
        return majorIncident;
    }

    public void setMajorIncident(Boolean majorIncident) {
        this.majorIncident = majorIncident;
    }

    public String getTenantIds() {
        return tenantIds;
    }

    public void setTenantIds(String tenantIds) {
        this.tenantIds = tenantIds;
    }

    public WfmFdBeanListData[] getFdBeanList() {
        return fdBeanList;
    }

    public void setFdBeanList(WfmFdBeanListData[] fdBeanList) {
        this.fdBeanList = fdBeanList;
    }

    public WfmContactBeanData getContactBean() {
        return contactBean;
    }

    public void setContactBean(WfmContactBeanData contactBean) {
        this.contactBean = contactBean;
    }

    public WfmCustomerBeanData getCustomerBean() {
        return customerBean;
    }

    public void setCustomerBean(WfmCustomerBeanData customerBean) {
        this.customerBean = customerBean;
    }
}
