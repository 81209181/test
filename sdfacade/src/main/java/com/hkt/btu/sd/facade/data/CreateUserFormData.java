package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

public class CreateUserFormData implements DataInterface {

    private String name;
    private String status;
    private String mobile;
    private String email;
    private String ldapDomain;
    private String userId;

    private String staffId;

    private String primaryRoleId;

    public String getPrimaryRoleId() {
        return primaryRoleId;
    }

    public void setPrimaryRoleId(String primaryRoleId) {
        this.primaryRoleId = primaryRoleId;
    }

    private List<String> userRoleIdList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLdapDomain() {
        return ldapDomain;
    }

    public void setLdapDomain(String ldapDomain) {
        this.ldapDomain = ldapDomain;
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
    
    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public List<String> getUserRoleIdList() {
        return userRoleIdList;
    }

    public void setUserRoleIdList(List<String> userRoleIdList) {
        this.userRoleIdList = userRoleIdList;
    }
}
