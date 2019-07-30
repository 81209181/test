package com.hkt.btu.noc.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.noc.core.service.bean.NocCronJobInstBean;
import com.hkt.btu.noc.facade.data.NocCronJobInstData;
import org.springframework.stereotype.Component;

@Component
public class NocCronJobInstDataPopulator extends AbstractDataPopulator<NocCronJobInstData> {

    public void populate(NocCronJobInstBean source, NocCronJobInstData target) {
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
