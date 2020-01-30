package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.populator.BtuCronJobProfileBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdCronJobEntity;
import com.hkt.btu.sd.core.service.bean.SdCronJobProfileBean;


public class SdCronJobProfileBeanPopulator extends BtuCronJobProfileBeanPopulator {
    public void populate(SdCronJobEntity source, SdCronJobProfileBean target) {
        super.populate(source, target);
    }
}