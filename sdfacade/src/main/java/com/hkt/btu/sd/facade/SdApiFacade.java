package com.hkt.btu.sd.facade;

public interface SdApiFacade {

    String getSiteInterfaceBean(String apiName);

    void reloadCached(String apiName);
}
