package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.SdAuditTrailService;
import com.hkt.btu.sd.facade.SdAuditTrailFacade;
import com.hkt.btu.sd.facade.data.SdUserData;

import javax.annotation.Resource;


public class SdAuditTrailFacadeImpl implements SdAuditTrailFacade {

    @Resource(name = "auditTrailService")
    SdAuditTrailService sdAuditTrailService;


    @Override
    public void insertViewUserAuditTrail(SdUserData sdUserData) {
        sdAuditTrailService.insertViewUserAuditTrail(sdUserData.getUserId());
    }
}
