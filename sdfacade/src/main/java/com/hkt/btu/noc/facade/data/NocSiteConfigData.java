package com.hkt.btu.noc.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class NocSiteConfigData implements DataInterface {

    // app
    private String appName;
    private String httpUrl;
    private String httpsUrl;
    private String givenDomain;

    // server
    private String contextPath;
    private String serverHostname;
    private String serverAddress;
    private String serverType;

    // login
    private Integer loginTriedLimit;
    private Integer passwordLifespanInDay;
    private Integer passwordResetOtpLifespanInMin;

    // cron job
    private String cronjobHostname;

    // email
    private String mailHost;
    private Integer mailPort;
    private String mailUsername;


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getHttpsUrl() {
        return httpsUrl;
    }

    public void setHttpsUrl(String httpsUrl) {
        this.httpsUrl = httpsUrl;
    }

    public String getGivenDomain() {
        return givenDomain;
    }

    public void setGivenDomain(String givenDomain) {
        this.givenDomain = givenDomain;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getServerHostname() {
        return serverHostname;
    }

    public void setServerHostname(String serverHostname) {
        this.serverHostname = serverHostname;
    }

    public String getCronjobHostname() {
        return cronjobHostname;
    }

    public void setCronjobHostname(String cronjobHostname) {
        this.cronjobHostname = cronjobHostname;
    }

    public Integer getLoginTriedLimit() {
        return loginTriedLimit;
    }

    public void setLoginTriedLimit(Integer loginTriedLimit) {
        this.loginTriedLimit = loginTriedLimit;
    }

    public Integer getPasswordLifespanInDay() {
        return passwordLifespanInDay;
    }

    public void setPasswordLifespanInDay(Integer passwordLifespanInDay) {
        this.passwordLifespanInDay = passwordLifespanInDay;
    }

    public Integer getPasswordResetOtpLifespanInMin() {
        return passwordResetOtpLifespanInMin;
    }

    public void setPasswordResetOtpLifespanInMin(Integer passwordResetOtpLifespanInMin) {
        this.passwordResetOtpLifespanInMin = passwordResetOtpLifespanInMin;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getMailHost() {
        return mailHost;
    }

    public void setMailHost(String mailHost) {
        this.mailHost = mailHost;
    }

    public Integer getMailPort() {
        return mailPort;
    }

    public void setMailPort(Integer mailPort) {
        this.mailPort = mailPort;
    }

    public String getMailUsername() {
        return mailUsername;
    }

    public void setMailUsername(String mailUsername) {
        this.mailUsername = mailUsername;
    }
}
