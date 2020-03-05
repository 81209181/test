package com.hkt.btu.sd.core.service.bean;

import java.time.LocalDateTime;

public class SdCheckCertBean {
    private String url;
    private LocalDateTime expiryDate;
    private long remainingDays;
    private String errorMsg;

    public SdCheckCertBean(String url, LocalDateTime expiryDate, long remainingDays, String errorMsg) {
        this.url = url;
        this.expiryDate = expiryDate;
        this.remainingDays = remainingDays;
        this.errorMsg = errorMsg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public long getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(long remainingDays) {
        this.remainingDays = remainingDays;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
