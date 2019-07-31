package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;

public class SdSiteConfigBean extends BtuSiteConfigBean {

    public class SERVER_TYPE {
        public static final String DEV = "DEV";
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
}
