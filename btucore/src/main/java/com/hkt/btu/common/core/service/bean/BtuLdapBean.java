package com.hkt.btu.common.core.service.bean;

public class BtuLdapBean extends BaseBean {


    private String ldapName;
    private String ldapServerUrl;
    private String ldapAttributeLoginName;
    private String principleName;
    private String userNameAttribute;
    private String displayName;
    private String hktStaffNumAttr;

    public String getLdapName() {
        return ldapName;
    }

    public void setLdapName(String ldapName) {
        this.ldapName = ldapName;
    }

    public String getLdapServerUrl() {
        return ldapServerUrl;
    }

    public void setLdapServerUrl(String ldapServerUrl) {
        this.ldapServerUrl = ldapServerUrl;
    }

    public String getLdapAttributeLoginName() {
        return ldapAttributeLoginName;
    }

    public void setLdapAttributeLoginName(String ldapAttributeLoginName) {
        this.ldapAttributeLoginName = ldapAttributeLoginName;
    }

    public String getPrincipleName() {
        return principleName;
    }

    public void setPrincipleName(String principleName) {
        this.principleName = principleName;
    }

    public String getUserNameAttribute() {
        return userNameAttribute;
    }

    public void setUserNameAttribute(String userNameAttribute) {
        this.userNameAttribute = userNameAttribute;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getHktStaffNumAttr() {
        return hktStaffNumAttr;
    }

    public void setHktStaffNumAttr(String hktStaffNumAttr) {
        this.hktStaffNumAttr = hktStaffNumAttr;
    }


}
