package com.hkt.btu.common.core.service;

import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;

public interface BtuAuditTrailService {
    void insertLoginAuditTrail(BtuUser btuUser);
    void insertLogoutAuditTrail(BtuUser btuUser);

    void insertLoginExceptionAuditTrail(BtuUser userDetails, String s);
}
