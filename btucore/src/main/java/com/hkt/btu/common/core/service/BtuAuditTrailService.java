package com.hkt.btu.common.core.service;

import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;

public interface BtuAuditTrailService {
    void insertLoginAuditTrail(BtuUser btuUser);

    void insertLogoutAuditTrail(BtuUser btuUser);

    void insertLoginExceptionAuditTrail(BtuUser btuUser, String exception);

    void insertAuditTrail(String action, String detail);
    void insertAuditTrail(Integer userId, String action, String detail);

    void insertViewUserAuditTrail(Integer userId);
    void insertViewRequesterAuditTrail(Integer requestId);
    void insertViewRequestVisitorAuditTrail(Integer visitorId);
}
