package com.hkt.btu.common.core.service;

import java.util.Map;

public interface BtuApiClientService {
    // cache
    Map<String, String> loadApiClientBean();
    void reloadCache();

    String getApiClientKey(String apiName);
    boolean checkApiClientKey(String apiName, String key);
}
