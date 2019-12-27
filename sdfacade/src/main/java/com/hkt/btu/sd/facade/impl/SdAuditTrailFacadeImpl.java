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

    @Override
    public void insertKickAuditTrail(String user, String name) {
        sdAuditTrailService.insertKickAuditTrail(user,name);
    }

    @Override
    public void insertViewTicketAuditTrail(String user, String ticketMasId) {
        sdAuditTrailService.insertViewTicketAuditTrail(user, ticketMasId);
    }

    @Override
    public void insertGetNgn3OneDayAdmin(String bsn, String companyId) {
        sdAuditTrailService.insertGetNgn3OneDayAdmin(bsn, companyId);
    }

    @Override
    public void insertResetNgn3Account(String dn) {
        sdAuditTrailService.insertResetNgn3Account(dn);
    }

    @Override
    public void insertViewApiAuthAuditTrail(String apiName) {
        sdAuditTrailService.insertViewApiAuthAuditTrail(apiName);
    }

    @Override
    public void insertRegenApiAuthAuditTrail(String apiName) {
        sdAuditTrailService.insertRegenApiAuthAuditTrail(apiName);
    }
}
