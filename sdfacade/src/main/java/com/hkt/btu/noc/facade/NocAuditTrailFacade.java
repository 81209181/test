package com.hkt.btu.noc.facade;


import com.hkt.btu.noc.facade.data.NocUserData;

public interface NocAuditTrailFacade {
    void insertViewUserAuditTrail(NocUserData nocUserData);
}
