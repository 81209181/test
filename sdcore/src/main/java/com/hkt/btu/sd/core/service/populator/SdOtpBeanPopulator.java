package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdOtpEntity;
import com.hkt.btu.sd.core.service.bean.SdOtpBean;


public class SdOtpBeanPopulator extends AbstractBeanPopulator<SdOtpBean> {
    public void populate(SdOtpEntity source, SdOtpBean target) {
        super.populate(source, target);

        target.setUserId(source.getUserId());
        target.setAction(source.getAction());
        target.setOtp(source.getOtp());
        target.setExpirydate(source.getExpirydate());
    }

}