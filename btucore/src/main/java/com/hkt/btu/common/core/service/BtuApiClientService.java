package com.hkt.btu.common.core.service;

import java.security.GeneralSecurityException;
import java.util.Map;

public interface BtuApiClientService {
    // cache
    Map<String, String> loadApiClientBean();
    void reloadCache();

    String getApiClientKey(String apiName);
    boolean checkApiClientKey(String apiName, String key);

    void regenerateApiClientKey(String apiName) throws GeneralSecurityException;
    void reloadApiClientKey(String apiName);
}
