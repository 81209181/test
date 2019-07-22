package com.hkt.btu.common.core.service.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class BtuSiteConfigBean extends BaseBean {

    public static final Integer DEFAULT_LOGIN_TRIED_LIMIT = 5;
    public static final Integer DEFAULT_PASSWORD_LIFESPAN_IN_DAY = 90;
    public static final Integer DEFAULT_PASSWORD_RESET_OTP_LIFESPAN_IN_MIN = 30;

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
