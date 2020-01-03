package com.hkt.btu.common.core.service.bean;


public class BtuCacheBean extends BaseBean {

    private String originServiceBeanName;
    private String originServiceMethodName;

    private int loadingPriority; // higher = earlier
    private boolean lazyInit; // pre-load or load on use

    private String cacheName;
    private Object cacheObject;



    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getOriginServiceBeanName() {
        return originServiceBeanName;
    }

    public void setOriginServiceBeanName(String originServiceBeanName) {
        this.originServiceBeanName = originServiceBeanName;
    }

    public String getOriginServiceMethodName() {
        return originServiceMethodName;
    }

    public void setOriginServiceMethodName(String originServiceMethodName) {
        this.originServiceMethodName = originServiceMethodName;
    }

    public int getLoadingPriority() {
        return loadingPriority;
    }

    public void setLoadingPriority(int loadingPriority) {
        this.loadingPriority = loadingPriority;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public Object getCacheObject() {
        return cacheObject;
    }

    public void setCacheObject(Object cacheObject) {
        this.cacheObject = cacheObject;
    }
}
