package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.ArrayList;

public class ItsmTenantData implements DataInterface {

    private int tenantId;
    private long custId;
    private String adminId;
    private String password;
    private String email;
    private String mobileNo;
    private int type;
    private String domainName;
    private String custName;
    private String custCode;
    private String isBesOrder;
    private String lastUpdId;

    private ArrayList<ItsmProfileData> resourcePoolList;


    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public long getCustId() {
        return custId;
    }

    public void setCustId(long custId) {
        this.custId = custId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getIsBesOrder() {
        return isBesOrder;
    }

    public void setIsBesOrder(String isBesOrder) {
        this.isBesOrder = isBesOrder;
    }

    public String getLastUpdId() {
        return lastUpdId;
    }

    public void setLastUpdId(String lastUpdId) {
        this.lastUpdId = lastUpdId;
    }

    public ArrayList<ItsmProfileData> getResourcePoolList() {
        return resourcePoolList;
    }

    public void setResourcePoolList(ArrayList<ItsmProfileData> resourcePoolList) {
        this.resourcePoolList = resourcePoolList;
    }

}
