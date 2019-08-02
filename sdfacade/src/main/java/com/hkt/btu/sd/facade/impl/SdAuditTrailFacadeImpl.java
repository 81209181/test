package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.service.BtuAuditTrailService;
import com.hkt.btu.sd.facade.SdAuditTrailFacade;
import com.hkt.btu.sd.facade.data.SdUserData;

import javax.annotation.Resource;


public class SdAuditTrailFacadeImpl implements SdAuditTrailFacade {

    @Resource(name = "auditTrailService")
    BtuAuditTrailService auditTrailService;


    @Override
    public void insertViewUserAuditTrail(SdUserData sdUserData) {
        auditTrailService.insertViewUserAuditTrail(sdUserData.getUserId());
    }

}
