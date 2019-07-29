package com.hkt.btu.noc.core.service.impl;

import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import com.hkt.btu.noc.core.dao.entity.NocAuditTrailEntity;
import com.hkt.btu.noc.core.dao.mapper.NocAuditTrailMapper;
import com.hkt.btu.noc.core.service.NocAuditTrailService;
import com.hkt.btu.noc.core.service.NocUserService;
import com.hkt.btu.noc.core.service.bean.NocUserBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;

public class NocAuditTrailServiceImpl implements NocAuditTrailService {
    private static final Logger LOG = LogManager.getLogger(NocAuditTrailServiceImpl.class);

    @Resource (name = "userService")
    NocUserService userService;

    @Resource
    NocAuditTrailMapper nocAuditTrailMapper;



    private void insertAuditTrail(BtuUser btuUser, String action, String detail) {
        // get uid
        Integer uid = null;
        if ( btuUser!=null && (btuUser.getUserBean() instanceof NocUserBean) ) {
            NocUserBean nocUserBean = (NocUserBean) btuUser.getUserBean();
            uid = nocUserBean.getUserId();
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
            nocAuditTrailMapper.insertAuditTrail(userId, action, detail);
        } catch (Exception e){
            LOG.error("Cannot insert audit trail: " + userId + ", " + action + ", " + detail);
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void insertViewUserAuditTrail(Integer userId) {
        String detail = String.format("%s", userId);
        insertAuditTrail(NocAuditTrailEntity.ACTION.VIEW_USER, detail);
    }

    @Override
    public void insertViewRequesterAuditTrail(Integer requesterId) {
        String detail = String.format("%s", requesterId);
        insertAuditTrail(NocAuditTrailEntity.ACTION.VIEW_REQUESTER, detail);
    }

    @Override
    public void insertViewRequestVisitorAuditTrail(Integer visitorAccessId) {
        String detail = String.format("%s", visitorAccessId);
        insertAuditTrail(NocAuditTrailEntity.ACTION.VIEW_VISITOR, detail);
    }

    public void insertLoginAuditTrail(BtuUser btuUser) {
        this.insertAuditTrail(btuUser, NocAuditTrailEntity.ACTION.LOGIN, null);
    }

    public void insertLogoutAuditTrail(BtuUser btuUser) {
        this.insertAuditTrail(btuUser, NocAuditTrailEntity.ACTION.LOGOUT, null);
    }


}
