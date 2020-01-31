package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.service.BtuAuditTrailService;

public interface SdAuditTrailService extends BtuAuditTrailService {

    // ticket
    void insertViewTicketAuditTrail(String user, String ticketMasId);

    // NGN3
    void insertGetNgn3OneDayAdmin(String bsn, String companyId);
    void insertResetNgn3Account(String dn);

}
