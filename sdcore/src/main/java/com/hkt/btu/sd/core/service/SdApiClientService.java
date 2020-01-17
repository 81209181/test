package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.service.BtuApiClientService;

import java.security.GeneralSecurityException;


public interface SdApiClientService extends BtuApiClientService {

    void regenerateApiClientKey(String apiName) throws GeneralSecurityException;
}
