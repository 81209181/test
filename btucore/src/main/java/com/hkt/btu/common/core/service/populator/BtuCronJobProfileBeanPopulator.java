package com.hkt.btu.common.core.service.populator;

import com.hkt.btu.common.core.dao.entity.BtuCronJobEntity;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import org.apache.commons.lang3.StringUtils;

public class BtuCronJobProfileBeanPopulator extends AbstractBeanPopulator<BtuCronJobProfileBean>{

    public void populate(BtuCronJobEntity source, BtuCronJobProfileBean target) {
        super.populate(source, target);

        target.setKeyGroup(source.getJobGroup());
        target.setKeyName(source.getJobName());
        target.setJobClass(source.getJobClass());

        target.setStatus(source.getStatus());
        target.setActive( StringUtils.equals(source.getStatus(), BtuCronJobEntity.STATUS.ACTIVE) );
        target.setMandatory( StringUtils.equals(source.getMandatory(), BtuCronJobEntity.MANDATORY.YES) );
        target.setCronExp(source.getCronExp());
    }
}
