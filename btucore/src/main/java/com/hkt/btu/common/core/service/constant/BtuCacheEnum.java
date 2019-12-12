package com.hkt.btu.common.core.service.constant;

import org.apache.commons.lang3.StringUtils;


public enum BtuCacheEnum {
    SERVICE_TYPE_LIST ("serviceTypeList", "123", "123", "")
    ;


    BtuCacheEnum(String cacheId, String objectTypeStr, String loadingClassBeanName, String loadingMethodStr) {
        this.cacheId = cacheId;
        this.objectTypeStr = objectTypeStr;
        this.loadingClassBeanName = loadingClassBeanName;
        this.loadingMethodStr = loadingMethodStr;
    }

    public static BtuCacheEnum getEnum(String cacheId) {
        for(BtuCacheEnum btuCacheEnum : values()){
            if(StringUtils.equals(cacheId, btuCacheEnum.cacheId)){
                return btuCacheEnum;
            }
        }
        return null;
    }

    private String cacheId;
    private String objectTypeStr;
    private String loadingClassBeanName;
    private String loadingMethodStr;


    public String getCacheId() {
        return cacheId;
    }

    public String getObjectTypeStr() {
        return objectTypeStr;
    }

    public String getLoadingClassBeanName() {
        return loadingClassBeanName;
    }

    public String getLoadingMethodStr() {
        return loadingMethodStr;
    }
}
