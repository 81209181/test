package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.impl.BtuAuditTrailServiceImpl;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import com.hkt.btu.sd.core.dao.entity.SdAuditTrailEntity;
import com.hkt.btu.sd.core.dao.mapper.SdAuditTrailMapper;
import com.hkt.btu.sd.core.service.SdAuditTrailService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdUserBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;

public class SdAuditTrailServiceImpl extends BtuAuditTrailServiceImpl implements SdAuditTrailService {
    private static final Logger LOG = LogManager.getLogger(SdAuditTrailServiceImpl.class);

    @Resource(name = "userService")
    SdUserService userService;

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
    public int houseKeep() {
        return sdAuditTrailMapper.houseKeep();
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


}
