package com.hkt.btu.common.core.service.populator;

import com.hkt.btu.common.core.service.bean.BtuCronJobInstBean;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class BtuCronJobInstBeanPopulator extends AbstractBeanPopulator<BtuCronJobInstBean> {

    public void populate(JobDetail jobDetail, BtuCronJobInstBean target) {
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

    public void populate(CronTrigger cronTrigger, BtuCronJobInstBean target) {
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

    public void populate(Trigger.TriggerState triggerState, BtuCronJobInstBean target) {
        if (triggerState != null) {
            if (triggerState == Trigger.TriggerState.PAUSED) {
                target.setPaused(true);
            } else {
                target.setPaused(false);
            }
        }
    }

}