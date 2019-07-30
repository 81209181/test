package com.hkt.btu.noc.core.service.impl;

import com.hkt.btu.noc.core.dao.entity.NocOtpEntity;
import com.hkt.btu.noc.core.dao.entity.NocUserEntity;
import com.hkt.btu.noc.core.dao.mapper.NocOtpMapper;
import com.hkt.btu.noc.core.service.NocOtpService;
import com.hkt.btu.noc.core.service.NocSiteConfigService;
import com.hkt.btu.noc.core.service.bean.NocOtpBean;
import com.hkt.btu.noc.core.service.populator.NocOtpBeanPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class NocOtpServiceImpl implements NocOtpService {
    @Resource
    NocOtpMapper nocOtpMapper;

    @Autowired
    NocOtpBeanPopulator nocOtpBeanPopulator;

    @Resource(name = "NocSiteConfigService")
    NocSiteConfigService nocSiteConfigService;


    @SuppressWarnings("SameParameterValue")
    private NocOtpBean getValidOtp(String otp, String action) {
        NocOtpEntity nocOtpEntity = nocOtpMapper.getOtp(otp, action);
        if(nocOtpEntity==null){
            return null;
        }

        NocOtpBean nocOtpBean = new NocOtpBean();
        nocOtpBeanPopulator.populate(nocOtpEntity, nocOtpBean);
        return nocOtpBean;
    }

    @Override
    public NocOtpBean getValidResetPwdOtp(String otp) {
        return getValidOtp(otp, NocOtpEntity.ACTION.RESET_PWD);
    }


    @SuppressWarnings("SameParameterValue")
    private String generateOtp(Integer userId, String action, Integer createby) {
        UUID uuid = UUID.randomUUID();
        String otp = uuid.toString();

        Integer otpLifespanInMin = nocSiteConfigService.getSiteConfigBean().getPasswordResetOtpLifespanInMin();

        LocalDateTime NOW = LocalDateTime.now();
        LocalDateTime expiryDate = NOW.plusMinutes(otpLifespanInMin);

        nocOtpMapper.insertOtp(userId, action, otp, expiryDate, createby);

        return otp;
    }

    @Override
    public String generatePwdResetOtp(Integer userId) {
        return generateOtp(userId, NocOtpEntity.ACTION.RESET_PWD, NocUserEntity.SYSTEM.USER_ID);
    }

    @Override
    public void expireOtp(String otp) {
        nocOtpMapper.expireOtp(otp);
    }

}
