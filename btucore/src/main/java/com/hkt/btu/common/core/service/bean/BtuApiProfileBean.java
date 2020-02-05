package com.hkt.btu.common.core.service.bean;

import javax.ws.rs.core.MultivaluedMap;

public class BtuApiProfileBean extends BaseBean {

    private String systemName;
    private String url;
    private String userName;
    private String password;

    private MultivaluedMap<String, Object> headerMap;
    private MultivaluedMap<String, Object> queryParamMap;
    private MultivaluedMap<String, Object> otherParamMap;

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

    public MultivaluedMap<String, Object> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(MultivaluedMap<String, Object> headerMap) {
        this.headerMap = headerMap;
    }

    public MultivaluedMap<String, Object> getQueryParamMap() {
        return queryParamMap;
    }

    public void setQueryParamMap(MultivaluedMap<String, Object> queryParamMap) {
        this.queryParamMap = queryParamMap;
    }

    public MultivaluedMap<String, Object> getOtherParamMap() {
        return otherParamMap;
    }

    public void setOtherParamMap(MultivaluedMap<String, Object> otherParamMap) {
        this.otherParamMap = otherParamMap;
    }
}
