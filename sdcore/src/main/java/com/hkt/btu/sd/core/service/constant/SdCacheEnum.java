package com.hkt.btu.sd.core.service.constant;

import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import org.apache.commons.lang3.StringUtils;

public enum SdCacheEnum {
    // servicedesk service
    SERVICE_TYPE_LIST ("Service Type List", 101, false,false,
            "serviceTypeService","loadServiceTypeList"),
    SERVICE_TYPE_OFFER_MAPPING ("Service Type Offer Mapping", 102, false,false,
            "serviceTypeService","loadServiceTypeOfferMapping"),

    // user role
    USER_ROLE_TREE ("User Role Tree", 120, false,false,
            "userRoleService","loadUserRoleTree"),

    // API Profile
    API_BES (SdConfigParamEntity.API.CONFIG_GROUP.API_BES, 201, false,true,
            "apiService","loadBesApiProfileBean"),
    API_ITSM (SdConfigParamEntity.API.CONFIG_GROUP.API_ITSM_RESTFUL, 202, false,true,
            "apiService","loadItsmApiProfileBean"),
    API_ITSM_RESTFUL (SdConfigParamEntity.API.CONFIG_GROUP.API_ITSM, 204, false,true,
            "apiService","loadItsmRestfulApiProfileBean"),
    API_NORARS (SdConfigParamEntity.API.CONFIG_GROUP.API_NORARS, 205, false,true,
            "apiService","loadNorarsApiProfileBean"),
    API_WFM (SdConfigParamEntity.API.CONFIG_GROUP.API_WFM, 206, false,true,
            "apiService","loadWfmApiProfileBean"),
    API_UT_CALL (SdConfigParamEntity.API.CONFIG_GROUP.API_UT_CALL, 207, false,true,
            "apiService","loadUtApiProfileBean"),
    API_OSS (SdConfigParamEntity.API.CONFIG_GROUP.API_OSS, 208, false,true,
            "apiService","loadOssApiProfileBean"),
    ;


    SdCacheEnum(String cacheName, int loadingPriority, boolean lazyInit,boolean sensitive,
                 String originServiceBeanName, String originServiceMethodName) {
        this.cacheName = cacheName;
        this.originServiceBeanName = originServiceBeanName;
        this.originServiceMethodName = originServiceMethodName;
        this.sensitive = sensitive;
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
