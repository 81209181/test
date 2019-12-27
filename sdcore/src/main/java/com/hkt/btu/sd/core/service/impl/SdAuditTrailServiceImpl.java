package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.exception.ClockOutSyncException;
import com.hkt.btu.common.core.service.BtuHealthCheckService;
import com.hkt.btu.common.core.service.impl.BtuAuditTrailServiceImpl;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import com.hkt.btu.sd.core.dao.entity.SdAuditTrailEntity;
import com.hkt.btu.sd.core.dao.mapper.SdAuditTrailMapper;
import com.hkt.btu.sd.core.service.SdAuditTrailService;
import com.hkt.btu.sd.core.service.SdUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.time.LocalDate;

public class SdAuditTrailServiceImpl extends BtuAuditTrailServiceImpl implements SdAuditTrailService {
    private static final Logger LOG = LogManager.getLogger(SdAuditTrailServiceImpl.class);

    @Resource(name = "userService")
    SdUserService userService;

    @Resource(name = "healthCheckService")
    BtuHealthCheckService healthCheckService;

    @Resource
    SdAuditTrailMapper sdAuditTrailMapper;


    private void insertAuditTrail(BtuUser btuUser, String action, String detail) {
        if (btuUser!=null) {
            this.insertAuditTrail(btuUser.getUsername(), action, detail);
        }
    }

    public void insertAuditTrail(String action, String detail) {
        BtuUser btuUser = userService.getCurrentUser();
        this.insertAuditTrail(btuUser, action, detail);
    }

    public void insertAuditTrail(String userId, String action, String detail) {
        LOG.info("Audit Trail: " + userId + ", " + action + ", " + detail);
        try {
            sdAuditTrailMapper.insertAuditTrail(userId, action, detail);
        } catch (Exception e) {
            LOG.error("Cannot insert audit trail: " + userId + ", " + action + ", " + detail);
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void insertViewUserAuditTrail(String userId) {
        String detail = String.format("%s", userId);
        insertAuditTrail(SdAuditTrailEntity.ACTION.VIEW_USER, detail);
    }

    @Override
    public void insertKickAuditTrail(String user, String kickBy) {
        insertAuditTrail(user,SdAuditTrailEntity.ACTION.KICK,String.format("By %s",kickBy));
    }

    @Override
    public void insertViewTicketAuditTrail(String user, String ticketMasId) {
        insertAuditTrail(user,SdAuditTrailEntity.ACTION.VIEW_TICKET,ticketMasId);
    }

    @Override
    public void insertGetNgn3OneDayAdmin(String bsn, String companyId) {
        String details = String.format("bsn: %s, companyId: %s", bsn, companyId);
        insertAuditTrail(SdAuditTrailEntity.ACTION.GET_NGN3_ADMIN_ACCOUNT, details);
    }

    @Override
    public void insertResetNgn3Account(String dn) {
        String details = String.format("dn: %s", dn);
        insertAuditTrail(SdAuditTrailEntity.ACTION.RESET_NGN3_ACCOUNT, details);
    }

    @Override
    public void insertDownLoadReportAuditTrail(String reportName, String userId) {
        String details = String.format("reportName: %s", reportName);
        insertAuditTrail(userId,SdAuditTrailEntity.ACTION.REPORT, details);
    }

    public void insertLoginAuditTrail(BtuUser btuUser) {
        insertAuditTrail(btuUser, SdAuditTrailEntity.ACTION.LOGIN, "SUCCESS");
    }

    public void insertLogoutAuditTrail(BtuUser btuUser) {
        insertAuditTrail(btuUser, SdAuditTrailEntity.ACTION.LOGOUT, "SUCCESS");
    }

    @Override
    public void insertLoginExceptionAuditTrail(BtuUser btuUser, String exception) {
        insertAuditTrail(btuUser, SdAuditTrailEntity.ACTION.LOGIN, exception);
    }

    @Override
    public void cleanAuditTrail() throws ClockOutSyncException {
        // Check Database date and JVM date in sync
        healthCheckService.checkTimeSync();

        // decide delete cutoff date
        LocalDate cutoffDate = LocalDate.now().withDayOfMonth(1).minusMonths(24);
        LOG.info("Cleaning audit trail before {}...", cutoffDate);

        // delete
        int deleteCount = sdAuditTrailMapper.cleanAuditTrail(cutoffDate);
        LOG.info("Deleted {} row(s) of audit trail.", deleteCount);
    }

    @Override
    public void insertViewApiAuthAuditTrail(String apiName) {
        insertAuditTrail(SdAuditTrailEntity.ACTION.VIEW_API_AUTH, apiName);
    }

    @Override
    public void insertRegenApiAuthAuditTrail(String apiName) {
        insertAuditTrail(SdAuditTrailEntity.ACTION.REGEN_API_AUTH, apiName);
    }
}
