package com.hkt.btu.common.core.service.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class BtuSiteConfigBean extends BaseBean {

    public static final Integer DEFAULT_LOGIN_TRIED_LIMIT = 5;
    public static final Integer DEFAULT_PASSWORD_LIFESPAN_IN_DAY = 90;
    public static final Integer DEFAULT_PASSWORD_RESET_OTP_LIFESPAN_IN_MIN = 15;

    public static final String DEFAULT_CRONJOB_HOSTNAME = "null.cronjob.hostname";

    public static final String DEFAULT_MAIL_HOST = "smtp.pccw.com";
    public static final Integer DEFAULT_MAIL_PORT = 25;
    public static final String DEFAULT_MAIL_USERNAME = "sdadmin@pccw.com";


    // server
    private String givenDomain;

    // cron job
    private String cronjobHostname;

    // login
    private Integer loginTriedLimit;
    private Integer passwordLifespanInDay;
    private Integer passwordResetOtpLifespanInMin;

    // email
    private String mailHost;
    private Integer mailPort;
    private String mailUsername;

    private String configGroup;
    private String configKey;
    private String configValue;
    private String configValueType;


    public class SERVER_TYPE {
        public static final String DEV = "DEV";
        public static final String VT = "VT";
        public static final String UAT = "UAT";
        public static final String PROD = "PROD";
        public static final String PROD_STANDBY = "PROD_SB";
    }

    private String contextPath;

    private String serverType;
    private String serverHostname;
    private String serverAddress;

    private String appHttpUrl;
    private String appHttpsUrl;
    private String appName;


    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getServerHostname() {
        return serverHostname;
    }

    public void setServerHostname(String serverHostname) {
        this.serverHostname = serverHostname;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getAppHttpUrl() {
        return appHttpUrl;
    }

    public void setAppHttpUrl(String appHttpUrl) {
        this.appHttpUrl = appHttpUrl;
    }

    public String getAppHttpsUrl() {
        return appHttpsUrl;
    }

    public void setAppHttpsUrl(String appHttpsUrl) {
        this.appHttpsUrl = appHttpsUrl;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }


    public String getConfigGroup() {
        return configGroup;
    }

    public void setConfigGroup(String configGroup) {
        this.configGroup = configGroup;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigValueType() {
        return configValueType;
    }

    public void setConfigValueType(String configValueType) {
        this.configValueType = configValueType;
    }


    public String getGivenDomain() {
        return givenDomain;
    }

    public void setGivenDomain(String givenDomain) {
        this.givenDomain = givenDomain;
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

    public String getCronjobHostname() {
        return cronjobHostname;
    }

    public void setCronjobHostname(String cronjobHostname) {
        this.cronjobHostname = cronjobHostname;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
