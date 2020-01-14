package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.SdApiClientService;
import com.hkt.btu.sd.core.service.SdAuditTrailService;
import com.hkt.btu.sd.facade.SdApiClientFacade;

import javax.annotation.Resource;
import java.security.GeneralSecurityException;

public class SdApiClientFacadeImpl implements SdApiClientFacade {

    @Resource(name = "apiClientService")
    SdApiClientService sdApiClientService;
    @Resource(name = "auditTrailService")
    SdAuditTrailService sdAuditTrailService;

    @Override
    public String getApiClientBean(String apiName){
        sdAuditTrailService.insertViewApiAuthAuditTrail(apiName);
        return sdApiClientService.getApiClientKey(apiName);
    }

    @Override
    public void regenerateApiClientKey(String apiName) throws GeneralSecurityException {
        // add audit trail
        sdAuditTrailService.insertRegenApiAuthAuditTrail(apiName);

        // re-generate a new key and update config param
        sdApiClientService.regenerateApiClientKey(apiName);

        // reload cache
        sdApiClientService.reloadCache();
    }
}
