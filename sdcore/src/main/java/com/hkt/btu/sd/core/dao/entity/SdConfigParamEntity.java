package com.hkt.btu.sd.core.dao.entity;


import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;

public class SdConfigParamEntity extends BtuConfigParamEntity {

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

    public static class USER_ROLE_CREATE_MAPPING{
        public static final String CONFIG_ROLE = "USER_ROLE_CREATE_MAPPING";
    }
}
