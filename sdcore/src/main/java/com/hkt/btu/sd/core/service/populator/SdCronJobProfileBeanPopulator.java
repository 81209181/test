package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdCronJobEntity;
import com.hkt.btu.sd.core.service.bean.SdCronJobProfileBean;
import org.apache.commons.lang3.StringUtils;

public class SdCronJobProfileBeanPopulator extends AbstractBeanPopulator<SdCronJobProfileBean> {
    public void populate(SdCronJobEntity source, SdCronJobProfileBean target) {
        super.populate(source, target);

        target.setKeyGroup(source.getJobGroup());
        target.setKeyName(source.getJobName());
        target.setJobClass(source.getJobClass());

        target.setStatus(source.getStatus());
        target.setActive( StringUtils.equals(source.getStatus(), SdCronJobEntity.STATUS.ACTIVE) );
        target.setMandatory( StringUtils.equals(source.getMandatory(), SdCronJobEntity.MANDATORY.YES) );
        target.setCronExp(source.getCronExp());
    }

}