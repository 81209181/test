package com.hkt.btu.common.core.service;

import java.security.GeneralSecurityException;
import java.util.Map;

public interface BtuApiClientService {
    // cache
    @SuppressWarnings("unused") // used in BtuCacheEnum
    Map<String, String> loadApiClientBean();
    @SuppressWarnings("unused") // used in BtuCacheEnum
    void reloadCache();

    String getApiClientKey(String apiName);
    boolean checkApiClientKey(String apiName, String key);

    void regenerateApiClientKey(String apiName) throws GeneralSecurityException;
    void reloadApiClientKey(String apiName);
}
