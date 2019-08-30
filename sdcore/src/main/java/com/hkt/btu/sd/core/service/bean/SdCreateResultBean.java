package com.hkt.btu.sd.core.service.bean;

public class SdCreateResultBean {

    private String userId;
    private String password;

    public static SdCreateResultBean of(String userId, String password) {
        return new SdCreateResultBean(userId, password);
    }

    private SdCreateResultBean(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
