package com.hkt.btu.sd.facade;

import java.security.GeneralSecurityException;

public interface SdApiClientFacade {

    String getApiClientBean(String apiName);

    void regenerateApiClientKey(String apiName) throws GeneralSecurityException;
}
