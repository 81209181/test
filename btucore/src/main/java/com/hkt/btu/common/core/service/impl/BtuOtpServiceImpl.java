package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.dao.entity.BtuOtpEntity;
import com.hkt.btu.common.core.dao.entity.BtuUserEntity;
import com.hkt.btu.common.core.dao.mapper.BtuOtpMapper;
import com.hkt.btu.common.core.service.BtuOtpService;
import com.hkt.btu.common.core.service.BtuSiteConfigService;
import com.hkt.btu.common.core.service.bean.BtuOtpBean;
import com.hkt.btu.common.core.service.populator.BtuOtpBeanPopulator;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;

public class BtuOtpServiceImpl implements BtuOtpService {

    @Resource
    BtuOtpMapper otpMapper;

    @Resource(name = "otpBeanPopulator")
    BtuOtpBeanPopulator otpBeanPopulator;

    @Resource(name = "siteConfigService")
    BtuSiteConfigService siteConfigService;

    @Override
    public BtuOtpBean getValidResetPwdOtp(String otp) {
        return getValidOtp(otp, BtuOtpEntity.ACTION.RESET_PWD);
    }

    @Override
    public String generatePwdResetOtp(Integer userId) {
        return generateOtp(userId, BtuOtpEntity.ACTION.RESET_PWD, BtuUserEntity.SYSTEM.USER_ID);
    }


    @Override
    public void expireOtp(String otp) {
        otpMapper.expireOtp(otp);
    }

    @SuppressWarnings("SameParameterValue")
    private String generateOtp(Integer userId, String action, Integer createBy) {
        UUID uuid = UUID.randomUUID();
        String otp = uuid.toString();

        Integer otpLifespanInMin = siteConfigService.getSiteConfigBean().getPasswordResetOtpLifespanInMin();

        LocalDateTime NOW = LocalDateTime.now();
        LocalDateTime expiryDate = NOW.plusMinutes(otpLifespanInMin);

        otpMapper.insertOtp(userId, action, otp, expiryDate, createBy);

        return otp;
    }

    @SuppressWarnings("SameParameterValue")
    private BtuOtpBean getValidOtp(String otp, String action) {
        BtuOtpEntity otpEntity = otpMapper.getOtp(otp, action);
        if (otpEntity == null) {
            return null;
        }

        BtuOtpBean sdOtpBean = new BtuOtpBean();
        otpBeanPopulator.populate(otpEntity, sdOtpBean);
        return sdOtpBean;
    }
}
