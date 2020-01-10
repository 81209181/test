package com.hkt.btu.sd.core.service;

import java.util.Map;

public interface SdApiClientService {

    Map<String, Object> loadApiClientBean();

    String getApiClientBean(String apiName);

    void reloadCached();
}
