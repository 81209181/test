package com.hkt.btu.common.core.service.bean;

import java.time.LocalDateTime;

public class BtuOtpBean extends BaseBean {
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
