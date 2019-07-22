package com.hkt.btu.noc.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

import java.time.LocalDateTime;

public class NocOtpEntity extends BaseEntity {
    public static class ACTION{
        public static final String RESET_PWD = "RESET_PWD";
    }


    private Integer userId;
    private String action;
    private String otp;
    private LocalDateTime expirydate;



    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(LocalDateTime expirydate) {
        this.expirydate = expirydate;
    }
}
