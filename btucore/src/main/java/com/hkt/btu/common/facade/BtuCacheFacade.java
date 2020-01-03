package com.hkt.btu.common.facade;

import com.hkt.btu.common.facade.data.BtuCacheProfileData;

import java.util.List;

public interface BtuCacheFacade {

    List<BtuCacheProfileData> getCacheProfileDataList();
    String getCachedObjectJson(String cacheName);
    String getSourceObjectJson(String cacheName);

    String reloadCacheByCacheName(String cacheName);

}
