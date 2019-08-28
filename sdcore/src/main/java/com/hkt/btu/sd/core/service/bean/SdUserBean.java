package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BtuUserBean;



public class SdUserBean extends BtuUserBean {

    public static String EMAIL_USER_ID_PREFIX = "E";

    private String userId;
    private String name;
    private String email;
    private String mobile;
    private String staffId;
    private String ldapDomain;

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

}
