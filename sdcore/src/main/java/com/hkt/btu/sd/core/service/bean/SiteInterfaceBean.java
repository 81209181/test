package com.hkt.btu.sd.core.service.bean;

import java.sql.Timestamp;

public class SiteInterfaceBean {


    public static class BES {
        public static final String SYSTEM_NAME = "BES";

        public static class API_HEADER {
            public static final String OPERATOR_ID = "OperatorId";
            public static final String CHANNEL_TYPE = "ChannelType";
            public static final String BE_ID = "BeId";
            public static final String X_APP_KEY = "X-APP-Key";
        }
    }

    public static class ITSM_RESTFUL {
        public static final String SYSTEM_NAME = "ITSM_RESTFUL";
    }

    public static class ITSM {
        public static final String SYSTEM_NAME = "ITSM";
    }

    public static class WFM {
        public static final String SYSTEM_NAME = "WFM";
    }


    private String systemName = "";
    private String url = "";
    private String userName = "";
    private String password = "";
    private Timestamp lastUpdatedDate;
    private String lastUpdatedBy = "";
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

    public Timestamp getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
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
