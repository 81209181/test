package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

import java.time.LocalDateTime;

public class SdUserEntity extends BaseEntity {

    public static class SYSTEM{
        public static final Integer USER_ID = 0;
    }

    public static class STATUS{
        public static final String ACTIVE = "A";
        public static final String LOCKED = "L";
        public static final String DISABLE = "D";
    }

    private Integer userId;
    private String name;
    private String status;
    private byte[] mobile;
    private String email;
    private Integer companyId;
    private byte[] staffId;

    private String password;
    private LocalDateTime passwordModifydate;
    private Integer loginTried;


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

    public byte[] getMobile() {
        return mobile;
    }

    public void setMobile(byte[] mobile) {
        this.mobile = mobile;
    }

    public byte[] getStaffId() {
        return staffId;
    }

    public void setStaffId(byte[] staffId) {
        this.staffId = staffId;
    }
}
