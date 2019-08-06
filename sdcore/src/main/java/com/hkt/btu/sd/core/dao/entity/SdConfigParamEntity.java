package com.hkt.btu.sd.core.dao.entity;


import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;

public class SdConfigParamEntity extends BtuConfigParamEntity {

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

    public static class CRONJOB{
        public static final String CONFIG_GROUP = "CRONJOB";

        public static final String CONFIG_KEY_ERROR_EMAIL = "errorEmail";
    }

    public static class NFM{
        public static final String CONFIG_GROUP = "NFM";

        public static final String CONFIG_KEY_EMAIL = "email";
    }

    public static class VISIT_LOC_EMAIL{
        public static final String CONFIG_GROUP = "VISIT_LOC_EMAIL";
    }

    public static class USER_GROUP_CREATE_MAPPING{
        public static final String CONFIG_GROUP = "USER_GROUP_CREATE_MAPPING";
    }
}
