package com.hkt.btu.common.core.service.bean;

public class BtuCacheInfoBean extends BaseBean{
    private String cacheId;
    private String cacheName;

    public String getCacheId() {
        return cacheId;
    }

    public void setCacheId(String cacheId) {
        this.cacheId = cacheId;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    @Override
    public String toString() {
        return "BtuCacheInfoBean{" +
                "cacheId='" + cacheId + '\'' +
                ", cacheName='" + cacheName + '\'' +
                '}';
    }
}
