package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.facade.impl.BtuApiClientFacadeImpl;
import com.hkt.btu.sd.core.service.SdAuditTrailService;
import com.hkt.btu.sd.facade.SdApiClientFacade;

import javax.annotation.Resource;
import java.security.GeneralSecurityException;

public class SdApiClientFacadeImpl extends BtuApiClientFacadeImpl implements SdApiClientFacade {


    @Resource(name = "auditTrailService")
    SdAuditTrailService sdAuditTrailService;



    @Override
    public String getApiAuthKey(String apiName){
        sdAuditTrailService.insertViewApiAuthAuditTrail(apiName);
        return super.getApiAuthKey(apiName);
    }

    @Override
    public void regenerateApiClientKey(String apiName) throws GeneralSecurityException {
        // add audit trail
        sdAuditTrailService.insertRegenApiAuthAuditTrail(apiName);

        // re-generate a new key and update config param
        super.regenerateApiClientKey(apiName);
    }


}
