package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.impl.BtuAuditTrailServiceImpl;
import com.hkt.btu.sd.core.dao.entity.SdAuditTrailEntity;
import com.hkt.btu.sd.core.dao.mapper.SdAuditTrailMapper;
import com.hkt.btu.sd.core.service.SdAuditTrailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.time.LocalDate;

public class SdAuditTrailServiceImpl extends BtuAuditTrailServiceImpl implements SdAuditTrailService {
    private static final Logger LOG = LogManager.getLogger(SdAuditTrailServiceImpl.class);

    @Resource
    SdAuditTrailMapper sdAuditTrailMapper;

    @Override
    protected void insertAuditTrailInternal(String userId, String action, String detail){
        LOG.info("Audit Trail: " + userId + ", " + action + ", " + detail);
        try {
            sdAuditTrailMapper.insertAuditTrail(userId, action, detail);
        } catch (Exception e) {
            LOG.error("Cannot insert audit trail: " + userId + ", " + action + ", " + detail);
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    protected void cleanAuditTrailInternal(LocalDate cutoffDate){
        int deleteCount = sdAuditTrailMapper.cleanAuditTrail(cutoffDate);
        LOG.info("Deleted {} row(s) of audit trail.", deleteCount);
    }

    @Override
    public void insertViewTicketAuditTrail(String user, String ticketMasId) {
        insertAuditTrailInternal(user, SdAuditTrailEntity.ACTION.VIEW_TICKET, ticketMasId);
    }

    @Override
    public void insertSearchInfoAuditTrail(String user, String searchKey, String searchValue) {
        String details = String.format("searchKey: %s, searchValue: %s", searchKey, searchValue);
        insertAuditTrailInternal(user, SdAuditTrailEntity.ACTION.SEARCH_INFO, details);
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
}
