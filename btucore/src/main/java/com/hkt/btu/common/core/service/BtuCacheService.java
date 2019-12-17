package com.hkt.btu.common.core.service;

public interface BtuCacheService {

    String getCache(String cacheId) throws Exception;

    void reloadCache(String cacheId) throws Exception;
}
