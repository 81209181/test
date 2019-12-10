package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.service.BtuAuditTrailService;
import org.quartz.JobExecutionException;

public interface SdAuditTrailService extends BtuAuditTrailService {
    void insertAuditTrail(String action, String detail);
    void insertAuditTrail(String userId, String action, String detail);

    void insertViewUserAuditTrail(String userId);

    void insertKickAuditTrail(String user, String clickBy);

    int houseKeep();

    void insertViewTicketAuditTrail(String user, String ticketMasId);

    void insertGetNgn3OneDayAdmin(String bsn, String companyId);
    void insertResetNgn3Account(String dn);

    void cleanAuditTrail() throws JobExecutionException;
}
