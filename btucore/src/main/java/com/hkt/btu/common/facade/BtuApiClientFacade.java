package com.hkt.btu.common.facade;

import com.hkt.btu.common.facade.data.BtuApiUserData;

import java.security.GeneralSecurityException;
import java.util.List;

public interface BtuApiClientFacade {

    List<BtuApiUserData> getApiUser();
    String getApiAuthKey(String apiName);

    void regenerateApiClientKey(String apiName) throws GeneralSecurityException;
    void reloadCache(String apiName);

}
