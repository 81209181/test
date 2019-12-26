package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.service.BtuAuditTrailService;

public interface SdAuditTrailService extends BtuAuditTrailService {
    // generic
    void insertAuditTrail(String action, String detail);
    void insertAuditTrail(String userId, String action, String detail);

    // user
    void insertViewUserAuditTrail(String userId);
    void insertKickAuditTrail(String user, String clickBy);

    // ticket
    void insertViewTicketAuditTrail(String user, String ticketMasId);

    // NGN3
    void insertGetNgn3OneDayAdmin(String bsn, String companyId);
    void insertResetNgn3Account(String dn);

    // house keep
    void cleanAuditTrail();

    void insertViewApiAuthAuditTrail(String apiName);

    void insertRegenApiAuthAuditTrail(String apiName);
}
