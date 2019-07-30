package com.hkt.btu.noc.facade.impl;

import com.hkt.btu.noc.core.service.NocAuditTrailService;
import com.hkt.btu.noc.facade.NocAuditTrailFacade;
import com.hkt.btu.noc.facade.data.NocUserData;

import javax.annotation.Resource;


public class NocAuditTrailFacadeImpl implements NocAuditTrailFacade {

    @Resource(name = "auditTrailService")
    NocAuditTrailService nocAuditTrailService;


    @Override
    public void insertViewUserAuditTrail(NocUserData nocUserData) {
        nocAuditTrailService.insertViewUserAuditTrail(nocUserData.getUserId());
    }

}
