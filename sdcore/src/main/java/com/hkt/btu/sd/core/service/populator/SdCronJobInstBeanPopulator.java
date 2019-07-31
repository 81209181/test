package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.service.bean.SdCronJobInstBean;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class SdCronJobInstBeanPopulator extends AbstractBeanPopulator<SdCronJobInstBean> {

    public void populate(JobDetail jobDetail, SdCronJobInstBean target) {
        if(jobDetail==null){
            return;
        }

        if(jobDetail.getKey() != null){
            target.setKeyGroup(jobDetail.getKey().getGroup());
            target.setKeyName(jobDetail.getKey().getName());
        }

        if(jobDetail.getJobClass() != null){
            target.setJobClass(jobDetail.getJobClass().getName());
        }
    }

    public void populate(CronTrigger cronTrigger, SdCronJobInstBean target) {
        Date previousFireDate = cronTrigger.getPreviousFireTime();
        LocalDateTime previousFireDateTime = previousFireDate==null ?
                null : LocalDateTime.ofInstant(previousFireDate.toInstant(), ZoneId.systemDefault());

        Date nextFireDate = cronTrigger.getNextFireTime();
        LocalDateTime nextFireDateTime = nextFireDate==null ?
                null : LocalDateTime.ofInstant(nextFireDate.toInstant(), ZoneId.systemDefault());

        target.setCurrentCronExp( cronTrigger.getCronExpression() );
        target.setLastRunTime(previousFireDateTime);
        target.setNextRunTime(nextFireDateTime);
    }

    public void populate(Trigger.TriggerState triggerState, SdCronJobInstBean target) {
        if (triggerState != null) {
            if(triggerState == Trigger.TriggerState.NORMAL){
                target.setPaused(false);
            } else {
                target.setPaused(true);
            }
        }
    }

}