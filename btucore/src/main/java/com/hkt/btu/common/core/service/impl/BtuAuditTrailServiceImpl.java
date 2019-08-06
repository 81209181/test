package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.dao.entity.BtuAuditTrailEntity;
import com.hkt.btu.common.core.service.BtuAuditTrailService;
import com.hkt.btu.common.core.service.BtuUserService;
import com.hkt.btu.common.core.dao.mapper.BtuAuditTrailMapper;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

public class BtuAuditTrailServiceImpl implements BtuAuditTrailService {
    private static final Logger LOG = LogManager.getLogger(BtuAuditTrailServiceImpl.class);

    @Resource(name = "userService")
    BtuUserService userService;

    @Resource
    BtuAuditTrailMapper auditTrailMapper;


    @Override
    public void insertLoginAuditTrail(BtuUser btuUser) {
        insertAuditTrail(btuUser, BtuAuditTrailEntity.ACTION.LOGIN, "SUCCESS");
    }

    @Override
    public void insertLogoutAuditTrail(BtuUser btuUser) {
        insertAuditTrail(btuUser, BtuAuditTrailEntity.ACTION.LOGOUT, null);
    }

    @Override
    public void insertLoginExceptionAuditTrail(BtuUser user, String exception) {
        insertAuditTrail(user, BtuAuditTrailEntity.ACTION.LOGIN, exception);
    }

    @Override
    public void insertAuditTrail(String action, String detail) {
        LOG.info("Audit Trail: {},{}", action, detail);
        BtuUser btuUser = userService.getCurrentUser();
        insertAuditTrail(btuUser, action, detail);
    }

    @Override
    public void insertAuditTrail(Integer userId, String action, String detail) {
        LOG.info("Audit Trail: {},{},{}", userId, action, detail);
        try {
            auditTrailMapper.insertAuditTrail(userId, action, detail);
        } catch (Exception e) {
            LOG.error("Cannot insert audit trail: {},{},{}",userId , action ,detail);
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void insertViewUserAuditTrail(Integer userId) {
        LOG.info("View Request Visitor Audit Trail -> User:{}",userId);
        String detail = String.format("%s", userId);
        insertAuditTrail(BtuAuditTrailEntity.ACTION.VIEW_USER, detail);
    }

    @Override
    public void insertViewRequesterAuditTrail(Integer requestId) {
        LOG.info("View Request Visitor Audit Trail -> Request ID:{}",requestId);
        String detail = String.format("%s", requestId);
        insertAuditTrail(BtuAuditTrailEntity.ACTION.VIEW_REQUESTER, detail);
    }

    @Override
    public void insertViewRequestVisitorAuditTrail(Integer visitorId) {
        LOG.info("View Request Visitor Audit Trail -> Visitor ID:{}",visitorId);
        String detail = String.format("%s", visitorId);
        insertAuditTrail(BtuAuditTrailEntity.ACTION.VIEW_VISITOR, detail);
    }

    private void insertAuditTrail(BtuUser btuUser, String action, String detail) {
        // get uid
        Integer uid = null;
        if (!StringUtils.isEmpty(btuUser) && !StringUtils.isEmpty(btuUser.getUserBean())){
            uid = btuUser.getUserBean().getUserId();
        }
        insertAuditTrail(uid, action, detail);
    }
}
