package com.hkt.btu.sd.core.service.constant;

import org.apache.commons.lang3.StringUtils;

public enum SdCacheEnum {
    SERVICE_TYPE_LIST ("Service Type List", 20, false,false,
            "serviceTypeService","loadServiceTypeList"),
    SERVICE_TYPE_OFFER_MAPPING ("Service Type Offer Mapping", 21, false,false,
            "serviceTypeService","loadServiceTypeOfferMapping"),
    USER_ROLE_TREE ("User Role Tree", 22, false,false,
            "userRoleService","loadUserRoleTree"),
    ;


    SdCacheEnum(String cacheName, int loadingPriority, boolean lazyInit,boolean isSensitive,
                 String originServiceBeanName, String originServiceMethodName) {
        this.cacheName = cacheName;
        this.originServiceBeanName = originServiceBeanName;
        this.originServiceMethodName = originServiceMethodName;
        this.loadingPriority = loadingPriority;
        this.lazyInit = lazyInit;
    }

    public static SdCacheEnum getEnum(String key) {
        for(SdCacheEnum cacheEnum : values()){
            if(StringUtils.equals(key, cacheEnum.cacheName)){
                return cacheEnum;
            }
        }
        return null;
    }

    private String cacheName;
    private int loadingPriority; // higher = earlier
    private boolean lazyInit; // pre-load or load on use
    private boolean isSensitive;

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
        return isSensitive;
    }
}
