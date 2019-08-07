package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdOtpEntity;
import com.hkt.btu.sd.core.dao.entity.SdUserEntity;
import com.hkt.btu.sd.core.dao.mapper.SdOtpMapper;
import com.hkt.btu.sd.core.service.SdOtpService;
import com.hkt.btu.sd.core.service.SdSiteConfigService;
import com.hkt.btu.sd.core.service.bean.SdOtpBean;
import com.hkt.btu.sd.core.service.populator.SdOtpBeanPopulator;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;

public class SdOtpServiceImpl implements SdOtpService {
    @Resource
    SdOtpMapper sdOtpMapper;

    @Resource(name = "otpBeanPopulator")
    SdOtpBeanPopulator sdOtpBeanPopulator;

    @Resource(name = "siteConfigService")
    SdSiteConfigService sdSiteConfigService;


    @SuppressWarnings("SameParameterValue")
    private SdOtpBean getValidOtp(String otp, String action) {
        SdOtpEntity sdOtpEntity = sdOtpMapper.getOtp(otp, action);
        if(sdOtpEntity==null){
            return null;
        }

        SdOtpBean sdOtpBean = new SdOtpBean();
        sdOtpBeanPopulator.populate(sdOtpEntity, sdOtpBean);
        return sdOtpBean;
    }

    @Override
    public SdOtpBean getValidResetPwdOtp(String otp) {
        return getValidOtp(otp, SdOtpEntity.ACTION.RESET_PWD);
    }


    @SuppressWarnings("SameParameterValue")
    private String generateOtp(Integer userId, String action, Integer createby) {
        UUID uuid = UUID.randomUUID();
        String otp = uuid.toString();

        Integer otpLifespanInMin = sdSiteConfigService.getSiteConfigBean().getPasswordResetOtpLifespanInMin();

        LocalDateTime NOW = LocalDateTime.now();
        LocalDateTime expiryDate = NOW.plusMinutes(otpLifespanInMin);

        sdOtpMapper.insertOtp(userId, action, otp, expiryDate, createby);

        return otp;
    }

    @Override
    public String generatePwdResetOtp(Integer userId) {
        return generateOtp(userId, SdOtpEntity.ACTION.RESET_PWD, SdUserEntity.SYSTEM.USER_ID);
    }

    @Override
    public void expireOtp(String otp) {
        sdOtpMapper.expireOtp(otp);
    }

    @Override
    public SdOtpBean getValidResetPwdOtp(Integer userId) {
        SdOtpEntity sdOtpEntity = sdOtpMapper.getOtpByUserId(userId, SdOtpEntity.ACTION.RESET_PWD);
        if(sdOtpEntity==null){
            return null;
        }

        SdOtpBean sdOtpBean = new SdOtpBean();
        sdOtpBeanPopulator.populate(sdOtpEntity, sdOtpBean);
        return sdOtpBean;
    }

}
