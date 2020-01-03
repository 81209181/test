package com.hkt.btu.common.core.service;

import com.hkt.btu.common.core.service.bean.BtuCacheBean;

import java.util.List;

public interface BtuCacheService {

    // setup
    void initCacheObjectMap();
    BtuCacheBean getNewCacheProfileBeanByCacheName(String cacheName);
    Object buildCacheObject(BtuCacheBean btuCacheBean);

    // interact
    void reloadAll();
    void reloadCachedObject(String cacheName);
    void deleteCachedObject(String cacheName);

    // get cached object
    List<BtuCacheBean> getAllCacheBean();
    Object getCachedObjectByCacheName(String cacheName);
    Object getSourceObjectByCacheName(String cacheName);
}
