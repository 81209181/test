package com.hkt.btu.noc.core.service;

import com.hkt.btu.common.core.service.BtuAuditTrailService;

public interface NocAuditTrailService extends BtuAuditTrailService {
    void insertAuditTrail(String action, String detail);
    void insertAuditTrail(Integer userId, String action, String detail);

    void insertViewUserAuditTrail(Integer userId);
    void insertViewRequesterAuditTrail(Integer requestId);
    void insertViewRequestVisitorAuditTrail(Integer visitorId);
}
