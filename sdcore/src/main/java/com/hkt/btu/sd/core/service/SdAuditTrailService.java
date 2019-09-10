package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.service.BtuAuditTrailService;

public interface SdAuditTrailService extends BtuAuditTrailService {
    void insertAuditTrail(String action, String detail);
    void insertAuditTrail(String userId, String action, String detail);

    void insertViewUserAuditTrail(String userId);
    void insertViewRequesterAuditTrail(Integer requestId);
    void insertViewRequestVisitorAuditTrail(Integer visitorId);

    void insertKickAuditTrail(String user, String clickBy);

    int houseKeep();
}
