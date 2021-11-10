package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.dao.entity.BtuAuditTrailEntity;
import com.hkt.btu.common.core.exception.BtuMissingImplException;
import com.hkt.btu.common.core.service.BtuAuditTrailService;
import com.hkt.btu.common.core.service.BtuHealthCheckService;
import com.hkt.btu.common.core.service.BtuUserService;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.time.LocalDate;


public class BtuAuditTrailServiceImpl implements BtuAuditTrailService {
    private static final Logger LOG = LogManager.getLogger(BtuAuditTrailServiceImpl.class);

    @Resource(name = "healthCheckService")
    BtuHealthCheckService healthCheckService;

    @Resource(name = "userService")
    BtuUserService userService;

    protected void insertAuditTrailInternal(String userId, String action, String detail){
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
    }

    protected void cleanAuditTrailInternal(LocalDate cutoffDate){
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
    }

    private void insertAuditTrail(BtuUser btuUser, String action, String detail) {
        if (btuUser==null) {
            // todo: check when will this happen?
            LOG.warn("Cannot log audit. Null BtuUser. (action={}, detail={})", action, detail);
        } else {
            insertAuditTrailInternal(btuUser.getUsername(), action, detail);
        }
    }

    @Override
    public void insertAuditTrail(String action, String detail) {
        BtuUser btuUser = userService.getCurrentUser();
        this.insertAuditTrail(btuUser, action, detail);
    }

    @Override
    public void insertLoginAuditTrail(BtuUser btuUser) {
        insertAuditTrail(btuUser, BtuAuditTrailEntity.ACTION.LOGIN, "SUCCESS");
    }

    @Override
    public void insertLogoutAuditTrail(BtuUser btuUser) {
        insertAuditTrail(btuUser, BtuAuditTrailEntity.ACTION.LOGOUT, "SUCCESS");
    }

    @Override
    public void insertLoginExceptionAuditTrail(BtuUser btuUser, String exception) {
        insertAuditTrail(btuUser, BtuAuditTrailEntity.ACTION.LOGIN, exception);
    }

    @Override
    public void insertKickAuditTrail(String user, String kickBy) {
        insertAuditTrailInternal(user, BtuAuditTrailEntity.ACTION.KICK, "By " + kickBy);
    }

    @Override
    public void insertViewUserAuditTrail(String userId) {
        String detail = String.format("%s", userId);
        insertAuditTrail(BtuAuditTrailEntity.ACTION.VIEW_USER, detail);
    }

    @Override
    public void insertDownloadReportAuditTrail(String reportFilePath, String userId) {
        String details = String.format("reportFilePath: %s", reportFilePath);
        insertAuditTrailInternal(userId, BtuAuditTrailEntity.ACTION.REPORT, details);
    }

    @Override
    public void insertViewApiAuthAuditTrail(String apiName) {
        insertAuditTrail(BtuAuditTrailEntity.ACTION.VIEW_API_AUTH, apiName);
    }

    @Override
    public void insertRegenApiAuthAuditTrail(String apiName) {
        insertAuditTrail(BtuAuditTrailEntity.ACTION.REGEN_API_AUTH, apiName);
    }

    @Override
    public void cleanAuditTrail() {
        // Check Database date and JVM date in sync
        healthCheckService.checkTimeSync();

        // decide delete cutoff date
        LocalDate cutoffDate = LocalDate.now().withDayOfMonth(1).minusMonths(24);
        LOG.info("Cleaning audit trail before {}...", cutoffDate);

        // delete
        cleanAuditTrailInternal(cutoffDate);
    }
}
