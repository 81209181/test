package com.hkt.btu.sd.core.service.bean;

public class SiteInterfaceBean {

    public static class API_BES {
        public static final String API_NAME = "API_BES";

        public static class API_HEADER {
            public static final String OPERATOR_ID = "OperatorId";
            public static final String CHANNEL_TYPE = "ChannelType";
            public static final String BE_ID = "BeId";
            public static final String X_APP_KEY = "X-APP-Key";
        }
    }

    public static class API_ITSM_RESTFUL {
        public static final String API_NAME = "API_ITSM_RESTFUL";
    }

    public static class API_ITSM {
        public static final String API_NAME = "API_ITSM";
    }

    public static class API_WFM {
        public static final String API_NAME = "API_WFM";
    }
    public static class API_NORARS{
        public static final String API_NAME = "API_NORARS";
    }

    public static class API_CONFIG_KEY {
        public static final String API_CONFIG_KEY_SYSTEM_NAME = "systemName";
        public static final String API_CONFIG_KEY_URL = "url";
        public static final String API_CONFIG_KEY_USER_NAME = "userName";
        public static final String API_CONFIG_KEY_PASSWORD = "password";
        public static final String API_CONFIG_KEY_X_APPKEY = "xAppkey";
        public static final String API_CONFIG_KEY_BE_ID = "beId";
        public static final String API_CONFIG_KEY_CHANNEL_TYPE = "channelType";
    }

    private String systemName;
    private String url;
    private String userName;
    private String password;
    private String xAppkey;
    private String beId;
    private String channelType;

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getxAppkey() {
        return xAppkey;
    }

    public void setxAppkey(String xAppkey) {
        this.xAppkey = xAppkey;
    }

    public String getBeId() {
        return beId;
    }

    public void setBeId(String beId) {
        this.beId = beId;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }


}
