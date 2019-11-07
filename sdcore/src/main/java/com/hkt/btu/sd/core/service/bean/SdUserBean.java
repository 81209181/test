package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BtuUserBean;


public class SdUserBean extends BtuUserBean {

    public static class CREATE_USER_PREFIX {
        public static final String PCCW_HKT_USER = "T";
        public static final String NON_PCCW_HKT_USER = "X";
    }

    private String userId;
    private String name;
    private String email;
    private String mobile;
    private String staffId;
    private String ldapDomain;
    private String domainEmail;
    private String primaryRoleId;

    public String getPrimaryRoleId() {
        return primaryRoleId;
    }

    public void setPrimaryRoleId(String primaryRoleId) {
        this.primaryRoleId = primaryRoleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLdapDomain() {
        return ldapDomain;
    }

    public void setLdapDomain(String ldapDomain) {
        this.ldapDomain = ldapDomain;
    }

    public String getDomainEmail() {
        return domainEmail;
    }

    public void setDomainEmail(String domainEmail) {
        this.domainEmail = domainEmail;
    }
}
