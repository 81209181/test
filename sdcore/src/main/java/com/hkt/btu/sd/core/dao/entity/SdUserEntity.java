package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

import java.time.LocalDateTime;
import java.util.Objects;

public class SdUserEntity extends BaseEntity {

    public static class SYSTEM{
        public static final String USER_ID = "SERVDESK";
    }

    public static class STATUS{
        public static final String ACTIVE = "A";
        public static final String LOCKED = "L";
        public static final String DISABLE = "D";
    }

    private String userId;
    private String name;
    private String status;
    private String email;
    private Integer companyId;
    private String mobile;
    private String staffId;
    //private byte[] mobile;
    //private byte[] staffId;

    private String ldapDomain;
    private String domainEmail;
    private String password;
    private LocalDateTime passwordModifydate;
    private Integer loginTried;

    private String primaryRoleId;


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getPasswordModifydate() {
        return passwordModifydate;
    }

    public void setPasswordModifydate(LocalDateTime passwordModifydate) {
        this.passwordModifydate = passwordModifydate;
    }

    public Integer getLoginTried() {
        return loginTried;
    }

    public void setLoginTried(Integer loginTried) {
        this.loginTried = loginTried;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
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

    public String getPrimaryRoleId() {
        return primaryRoleId;
    }

    public void setPrimaryRoleId(String primaryRoleId) {
        this.primaryRoleId = primaryRoleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SdUserEntity that = (SdUserEntity) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
