package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.service.BtuAuditTrailService;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service("BtuAuditTrailService")
public class BtuAuditTrailServiceImpl implements BtuAuditTrailService {
    private static final Logger LOG = LogManager.getLogger(BtuAuditTrailServiceImpl.class);

    @Override
    public void insertLoginAuditTrail(BtuUser user) {
        LOG.info("User:" +user.getUsername() + " successfully login.");
    }

    @Override
    public void insertLogoutAuditTrail(BtuUser user) {
        LOG.info("User:" +user.getUsername() + " successfully logout.");
    }

    @Override
    public void insertLoginExceptionAuditTrail(BtuUser user, String exception) {
        LOG.error("User:" + user.getUsername() + " ,login error:" + exception);
    }
}
