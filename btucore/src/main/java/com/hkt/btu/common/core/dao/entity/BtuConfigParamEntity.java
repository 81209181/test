package com.hkt.btu.common.core.dao.entity;

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
        public static final String CONFIG_KEY_VT_HOSTNAME = "vtHostname";

        public static final String CONFIG_KEY_MAIL_HOST = "mailHost";
        public static final String CONFIG_KEY_MAIL_PORT = "mailPort";
        public static final String CONFIG_KEY_MAIL_USERNAME = "mailUsername";

        public static final String CONFIG_KEY_SYSTEM_SUPPORT = "systemSupportEmail";

        public static final String CONFIG_KEY_PROXY_HOST = "proxyHost";
        public static final String CONFIG_KEY_PROXY_PORT = "proxyPort";
    }

    public static class CHECK_CERT_JOB {
        public static final String CONFIG_GROUP = "CHECK_CERT_JOB";
    }

    public static class CRON_JOB {
        public static final String CONFIG_GROUP = "CRONJOB";
        public static final String CONFIG_KEY_MULTIPLE_RECIPIENT = "multipleRecipient";
    }

    public static class SEARCH_KEY_TYPE_MAPPING {
        public static final String CONFIG_GROUP = "SEARCH_KEY_TYPE_MAPPING";
    }

    public static class API_CLIENT {
        public static final String CONFIG_GROUP = "API_CLIENT";
    }

    public static class API {
        public static class CONFIG_KEY {
            public static final String PREFIX_HEADER = "header.";
            public static final String PREFIX_QUERY_PARAM = "queryParam.";

            public static final String SYSTEM_NAME = "systemName";
            public static final String URL = "url";
            public static final String USER_NAME = "userName";
            public static final String PASSWORD = "password";
            public static final String BYPASS_SSL = "bypassSsl";
        }
    }

    public static class TYPE {
        public static final String STRING = "String";
        public static final String INTEGER = "Integer";
        public static final String DOUBLE = "Double";
        public static final String BOOLEAN = "Boolean";
        public static final String LOCAL_DATE_TIME = "LocalDateTime";
    }

    public static class ENCRYPT{
        public static final String YES = "Y";
        public static final String NO = "N";
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
