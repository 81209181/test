package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

public class SdUserData implements DataInterface {

    private Integer userId;
    private String name;
    private String mobile;
    private String email;
    private String ldapDomain;

    private String status;
    private String passwordModifyDate;
    private Integer loginTried;

    private Integer companyId;
    private String companyName;
    private String staffId;

    private List<String> userGroupList;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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

    public String getPasswordModifyDate() {
        return passwordModifyDate;
    }

    public void setPasswordModifyDate(String passwordModifyDate) {
        this.passwordModifyDate = passwordModifyDate;
    }

    public Integer getLoginTried() {
        return loginTried;
    }

    public void setLoginTried(Integer loginTried) {
        this.loginTried = loginTried;
    }

    public List<String> getUserGroupList() {
        return userGroupList;
    }

    public void setUserGroupList(List<String> userGroupList) {
        this.userGroupList = userGroupList;
    }

    public String getLdapDomain() {
        return ldapDomain;
    }

    public void setLdapDomain(String ldapDomain) {
        this.ldapDomain = ldapDomain;
    }
}
