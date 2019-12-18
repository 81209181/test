package com.hkt.btu.common.core.service.constant;

import com.hkt.btu.common.core.service.bean.BtuCacheInfoBean;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public enum BtuCacheEnum {
    C1 ("Service Type Offer Mapping","serviceTypeService", "getServiceTypeOfferMappingBean", "reloadServiceTypeOfferMapping"),
    C2 ("Service Type List","serviceTypeService","getServiceTypeList","reloadServiceTypeList"),
    C3 ("Site Config","","",""),
    C4 ("User Role","","","")
    ;

    BtuCacheEnum(String cacheName, String serviceName, String cacheBeanMethodName, String reloadMethodName) {
        this.cacheName = cacheName;
        this.serviceName = serviceName;
        this.cacheBeanMethodName = cacheBeanMethodName;
        this.reloadMethodName = reloadMethodName;
    }

    public static List<BtuCacheInfoBean> getCacheInfoList() {
        return Stream.of(values()).map(btuCacheEnum -> {
            BtuCacheInfoBean bean = new BtuCacheInfoBean();
            bean.setCacheId(btuCacheEnum.toString());
            bean.setCacheName(btuCacheEnum.getCacheName());
            return bean;
        }).collect(Collectors.toList());
    }

    private String cacheName;
    private String serviceName;
    private String cacheBeanMethodName;
    private String reloadMethodName;


    public String getCacheName() {
        return cacheName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getCacheBeanMethodName() {
        return cacheBeanMethodName;
    }

    public String getReloadMethodName() {
        return reloadMethodName;
    }
}
