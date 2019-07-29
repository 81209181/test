package com.hkt.btu.noc.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.noc.core.dao.entity.NocOtpEntity;
import com.hkt.btu.noc.core.service.bean.NocOtpBean;


public class NocOtpBeanPopulator extends AbstractBeanPopulator<NocOtpBean> {
    public void populate(NocOtpEntity source, NocOtpBean target) {
        super.populate(source, target);

        target.setUserId(source.getUserId());
        target.setAction(source.getAction());
        target.setOtp(source.getOtp());
        target.setExpirydate(source.getExpirydate());
    }

}