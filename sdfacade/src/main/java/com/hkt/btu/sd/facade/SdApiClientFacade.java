package com.hkt.btu.sd.facade;

public interface SdApiClientFacade {

    String getApiClientBean(String apiName);

    void reloadCached(String apiName);
}
