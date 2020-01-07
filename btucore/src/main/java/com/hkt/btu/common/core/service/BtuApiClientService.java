package com.hkt.btu.common.core.service;

public interface BtuApiClientService {

    String getApiClientBean(String apiName);

    void reloadCached(String apiName);
}
