package com.hkt.btu.sd.core.service;

public interface SdApiClientService {

    String getApiClientBean(String apiName);

    void reloadCached(String apiName);
}
