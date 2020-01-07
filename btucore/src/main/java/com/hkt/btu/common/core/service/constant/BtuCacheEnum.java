package com.hkt.btu.common.core.service.constant;

import org.apache.commons.lang3.StringUtils;



public enum BtuCacheEnum {
    RESOURCE_MAP ("Security Resource Map", 100, false,
        "customBtuSecurityMetadataSource","buildResourceMapFromDb"),
    SITE_CONFIG_MAP("Site Config Map",101,true,
            "siteConfigService","getSiteConfigBean"),
    SENSITIVE_DATA("Sensitive Data Map",102,false,
            "sensitiveDataService","getCachedKeyMap")
    ;


    BtuCacheEnum(String cacheName, int loadingPriority, boolean lazyInit,
                 String originServiceBeanName, String originServiceMethodName) {
        this.cacheName = cacheName;
        this.originServiceBeanName = originServiceBeanName;
        this.originServiceMethodName = originServiceMethodName;
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
}
