package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.data.SdApiUserData;

import java.security.GeneralSecurityException;
import java.util.List;

public interface SdApiClientFacade {

    List<SdApiUserData> getApiUser();

    String getApiClientBean(String apiName);

    void regenerateApiClientKey(String apiName) throws GeneralSecurityException;

    void reloadCache(String apiName);
}
