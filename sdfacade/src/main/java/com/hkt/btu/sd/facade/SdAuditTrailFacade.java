package com.hkt.btu.sd.facade;


import com.hkt.btu.sd.facade.data.SdUserData;

public interface SdAuditTrailFacade {

    // user
    void insertViewUserAuditTrail(SdUserData sdUserData);
    void insertKickAuditTrail(String user, String name);

    // ticket
    void insertViewTicketAuditTrail(String user, String ticketMasId);

    // NGN3
    void insertGetNgn3OneDayAdmin(String bsn, String companyId);
    void insertResetNgn3Account(String dn);

    void insertViewApiAuthAuditTrail(String apiName);
    void insertRegenApiAuthAuditTrail(String apiName);
}
