package com.hkt.btu.common.core.service.populator;

import com.hkt.btu.common.core.dao.entity.BtuOtpEntity;
import com.hkt.btu.common.core.service.bean.BtuOtpBean;

public class BtuOtpBeanPopulator extends AbstractBeanPopulator<BtuOtpBean>{

    public void populate(BtuOtpEntity source, BtuOtpBean target) {
        super.populate(source, target);

        target.setUserId(source.getUserId());
        target.setAction(source.getAction());
        target.setOtp(source.getOtp());
        target.setExpirydate(source.getExpirydate());
    }
}
