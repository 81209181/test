package com.hkt.btu.common.core.dao.entity;

import java.util.ArrayList;
import java.util.List;

public class BtuConfigParamEntity extends BaseEntity {

    // reserved config groups
    public static class SITE{
        public static final String CONFIG_GROUP = "SITE";

        public static final String CONFIG_KEY_APP_NAME = "appName";

        public static final String CONFIG_KEY_GIVEN_DOMAIN = "givenDomain";

        public static final String CONFIG_KEY_LOGIN_TRIED_LIMIT = "loginTriedLimit";
        public static final String CONFIG_KEY_PWD_LIFESPAN_IN_DAY = "passwordLifespanInDay";
        public static final String CONFIG_KEY_PWD_REST_OTP_LIFESPAN_IN_MIN = "passwordResetOtpLifespanInMin";

        public static final String CONFIG_KEY_CRONJOB_HOSTNAME = "cronjobHostname";
        public static final String CONFIG_KEY_PROD_HOSTNAME = "prodHostname";
        public static final String CONFIG_KEY_PROD_STANDBY_HOSTNAME = "prodStandbyHostname";
        public static final String CONFIG_KEY_UAT_HOSTNAME = "uatHostname";

        public static final String CONFIG_KEY_MAIL_HOST = "mailHost";
        public static final String CONFIG_KEY_MAIL_PORT = "mailPort";
        public static final String CONFIG_KEY_MAIL_USERNAME = "mailUsername";
    }

    public static class TYPE {
        public static final String STRING = "String";
        public static final String INTEGER = "Integer";
        public static final String DOUBLE = "Double";
        public static final String BOOLEAN = "Boolean";
        public static final String LOCAL_DATE_TIME = "LocalDateTime";
    }

    public static List<String> getConfigTypeList() {
        List<String> list = new ArrayList<>();
        list.add(TYPE.STRING);
        list.add(TYPE.INTEGER);
        list.add(TYPE.DOUBLE);
        list.add(TYPE.BOOLEAN);
        list.add(TYPE.LOCAL_DATE_TIME);
        return list;
    }

    private String configGroup;
    private String configKey;
    private String configValue;
    private String configValueType;
    private String encrypt;

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

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }
}
