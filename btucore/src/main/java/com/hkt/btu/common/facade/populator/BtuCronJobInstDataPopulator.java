package com.hkt.btu.common.facade.populator;

import com.hkt.btu.common.core.service.bean.BtuCronJobInstBean;
import com.hkt.btu.common.facade.data.BtuCronJobInstData;

public class BtuCronJobInstDataPopulator extends AbstractDataPopulator<BtuCronJobInstData> {
    public void populate(BtuCronJobInstBean source, BtuCronJobInstData target) {
        target.setKeyGroup(source.getKeyGroup());
        target.setKeyName(source.getKeyName());
        target.setJobClass(source.getJobClass());
        target.setCronExpression(source.getCurrentCronExp());
        target.setLastRunTime(source.getLastRunTime());
        target.setNextRunTime(source.getNextRunTime());
        target.setPaused(source.getPaused());

        if(target.getPaused()){
            target.setNextRunTime(null);
        }
    }
}
