package com.hkt.btu.common.core.service.constant;

import org.apache.commons.lang3.StringUtils;



public enum BtuCacheEnum {
    RESOURCE_MAP ("Security Resource Map", 10, false,false,
        "customBtuSecurityMetadataSource","buildResourceMapFromDb"),
    SITE_CONFIG_MAP("Site Config Map",11,false,false,
            "siteConfigService","loadSiteConfigBean"),
    // sensitive data temporally changed to lazyInit (load on request) for solving catalina properties read after cache built problem
    SENSITIVE_DATA("Sensitive Data Map",12,false,true,
            "sensitiveDataService","loadCachedKey"),
    ;


    BtuCacheEnum(String cacheName, int loadingPriority, boolean lazyInit,boolean sensitive,
                 String originServiceBeanName, String originServiceMethodName) {
        this.cacheName = cacheName;
        this.originServiceBeanName = originServiceBeanName;
        this.originServiceMethodName = originServiceMethodName;
        this.sensitive = sensitive;
        this.loadingPriority = loadingPriority;
        this.lazyInit = lazyInit;
    }

    public static BtuCacheEnum getEnum(String key) {
        for(BtuCacheEnum cacheEnum : values()){
            if(StringUtils.equals(key, cacheEnum.cacheName)){
                return cacheEnum;
            }
        }
        return null;
    }

    private String cacheName;
    private int loadingPriority; // higher = earlier
    private boolean lazyInit; // pre-load or load on use
    private boolean sensitive;
    private String originServiceBeanName;
    private String originServiceMethodName;



    public String getCacheName() {
        return cacheName;
    }

    public String getOriginServiceBeanName() {
        return originServiceBeanName;
    }

    public String getOriginServiceMethodName() {
        return originServiceMethodName;
    }

    public int getLoadingPriority() {
        return loadingPriority;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public boolean isSensitive() {
        return sensitive;
    }
}
