package com.hkt.btu.sd.facade;


import com.hkt.btu.sd.facade.data.SdUserData;

public interface SdAuditTrailFacade {
    void insertViewUserAuditTrail(SdUserData sdUserData);

    void insertKickAuditTrail(String user, String name);

    void insertViewTicketAuditTrail(String user, String ticketMasId);
}
