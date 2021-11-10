package com.hkt.btu.common.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hkt.btu.common.facade.data.BtuCacheProfileData;

import java.util.List;

public interface BtuCacheFacade {

    List<BtuCacheProfileData> getCacheProfileDataList();
    String getCachedObjectJson(String cacheName) throws JsonProcessingException;
    String getSourceObjectJson(String cacheName) throws JsonProcessingException;

    String reloadCacheByCacheName(String cacheName);

    BtuCacheProfileData getCacheProfileDataByCacheName(String cacheName);

}
