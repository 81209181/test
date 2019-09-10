package com.hkt.btu.sd.facade;


import com.hkt.btu.sd.facade.data.SdUserData;

public interface SdAuditTrailFacade {
    void insertViewUserAuditTrail(SdUserData sdUserData);

    void insertClickAuditTrail(String user, String name);
}
