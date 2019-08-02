package com.hkt.btu.sd.facade.populator;


import com.hkt.btu.common.core.service.bean.BtuCronJobInstBean;
import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.facade.data.SdCronJobInstData;

public class SdCronJobInstDataPopulator extends AbstractDataPopulator<SdCronJobInstData> {

    public void populate(BtuCronJobInstBean source, SdCronJobInstData target) {
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
