package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class ChangeUserTypeFormData implements DataInterface {

    public static final String PCCW_HKT_USER = "pccw-hkt-user";
    public static final String LDAP_USER = "ldap-user";

    private String name;
    private String status;
    private String mobile;
    private String email;
    private String ldapDomain;
    private String userId;
    private String newUserId;

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

    public String getLdapDomain() {
        return ldapDomain;
    }

    public void setLdapDomain(String ldapDomain) {
        this.ldapDomain = ldapDomain;
    }

    public String getNewUserId() {
        return newUserId;
    }

    public void setNewUserId(String newUserId) {
        this.newUserId = newUserId;
    }
}
