package com.hkt.btu.common.core.service;

import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;

public interface BtuAuditTrailService {

    // generic
    void insertAuditTrail(String action, String detail);

    // login
    void insertLoginAuditTrail(BtuUser btuUser);
    void insertLogoutAuditTrail(BtuUser btuUser);
    void insertLoginExceptionAuditTrail(BtuUser btuUser, String exception);
    void insertKickAuditTrail(String user, String kickBy);

    // user
    void insertViewUserAuditTrail(String userId);

    //download Report
    void insertDownloadReportAuditTrail(String reportFilePath, String userId);

    // api client auth key
    void insertViewApiAuthAuditTrail(String apiName);
    void insertRegenApiAuthAuditTrail(String apiName);

    // house keep
    void cleanAuditTrail();
}
