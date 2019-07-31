package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import com.hkt.btu.sd.core.dao.entity.SdAuditTrailEntity;
import com.hkt.btu.sd.core.dao.mapper.SdAuditTrailMapper;
import com.hkt.btu.sd.core.service.SdAuditTrailService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdUserBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

public class SdAuditTrailServiceImpl implements SdAuditTrailService {
    private static final Logger LOG = LogManager.getLogger(SdAuditTrailServiceImpl.class);

    @Resource (name = "userService")
    SdUserService userService;

    @Resource
    SdAuditTrailMapper sdAuditTrailMapper;



    private void insertAuditTrail(BtuUser btuUser, String action, String detail) {
        // get uid
        Integer uid = null;
        if ( btuUser!=null && (btuUser.getUserBean() instanceof SdUserBean) ) {
            SdUserBean sdUserBean = (SdUserBean) btuUser.getUserBean();
            uid = sdUserBean.getUserId();
        }
        this.insertAuditTrail(uid, action, detail);
    }

    public void insertAuditTrail(String action, String detail) {
        BtuUser btuUser = userService.getCurrentUser();
        this.insertAuditTrail(btuUser, action, detail);
    }

    public void insertAuditTrail(Integer userId, String action, String detail) {
        LOG.info("Audit Trail: " + userId + ", " + action + ", " + detail);
        try {
            sdAuditTrailMapper.insertAuditTrail(userId, action, detail);
        } catch (Exception e){
            LOG.error("Cannot insert audit trail: " + userId + ", " + action + ", " + detail);
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void insertViewUserAuditTrail(Integer userId) {
        String detail = String.format("%s", userId);
        insertAuditTrail(SdAuditTrailEntity.ACTION.VIEW_USER, detail);
    }

    @Override
    public void insertViewRequesterAuditTrail(Integer requesterId) {
        String detail = String.format("%s", requesterId);
        insertAuditTrail(SdAuditTrailEntity.ACTION.VIEW_REQUESTER, detail);
    }

    @Override
    public void insertViewRequestVisitorAuditTrail(Integer visitorAccessId) {
        String detail = String.format("%s", visitorAccessId);
        insertAuditTrail(SdAuditTrailEntity.ACTION.VIEW_VISITOR, detail);
    }

    public void insertLoginAuditTrail(BtuUser btuUser) {
        this.insertAuditTrail(btuUser, SdAuditTrailEntity.ACTION.LOGIN, null);
    }

    public void insertLogoutAuditTrail(BtuUser btuUser) {
        this.insertAuditTrail(btuUser, SdAuditTrailEntity.ACTION.LOGOUT, null);
    }


}
