package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.impl.BtuSchedulerServiceImpl;
import com.hkt.btu.sd.core.service.SdCronJobProfileService;
import com.hkt.btu.sd.core.service.SdSchedulerService;
import com.hkt.btu.sd.core.service.bean.SdCronJobInstBean;
import com.hkt.btu.sd.core.service.bean.SdSqlReportBean;
import com.hkt.btu.sd.core.service.populator.SdCronJobInstBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

public class SdSchedulerServiceImpl extends BtuSchedulerServiceImpl implements SdSchedulerService {
    private static final Logger LOG = LogManager.getLogger(SdSchedulerServiceImpl.class);


    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Resource(name = "cronJobProfileService")
    SdCronJobProfileService sdCronJobProfileService;

    @Resource(name = "cronJobInstBeanPopulator")
    SdCronJobInstBeanPopulator sdCronJobInstBeanPopulator;

    @Override
    public List<SdCronJobInstBean> getAllCronJobInstance() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        List<SdCronJobInstBean> jobBeanList = new LinkedList<>();
        for (String groupName : scheduler.getJobGroupNames()) {
            if (!groupName.equals(SdSqlReportBean.KEY_GROUP)) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    SdCronJobInstBean jobBean = getCronJobInstance(jobKey);
                    jobBeanList.add(jobBean);
                }
            }
        }
        return jobBeanList;
    }

    private SdCronJobInstBean getCronJobInstance(JobKey jobKey) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        SdCronJobInstBean jobBean = new SdCronJobInstBean();

        JobDetail jobDetail = getJobDetail(jobKey);
        sdCronJobInstBeanPopulator.populate(jobDetail, jobBean);

        List<? extends Trigger> triggerList = scheduler.getTriggersOfJob(jobKey);
        if (CollectionUtils.isEmpty(triggerList)) {
            jobBean.setPaused(true);
        } else {
            List<CronTrigger> cronTriggerList = new LinkedList<>();
            for (Trigger trigger : triggerList) {
                if (trigger instanceof CronTrigger) {
                    cronTriggerList.add((CronTrigger) trigger);
                }
            }

            if (CollectionUtils.size(cronTriggerList) == 1) {
                CronTrigger cronTrigger = cronTriggerList.get(0);
                sdCronJobInstBeanPopulator.populate(cronTrigger, jobBean);

                Trigger.TriggerState triggerState = scheduler.getTriggerState(cronTrigger.getKey());
                sdCronJobInstBeanPopulator.populate(triggerState, jobBean);
            } else {
                jobBean.setCurrentCronExp("Unsupported for multi-cron-trigger job.");
                jobBean.setLastRunTime(null);
                jobBean.setNextRunTime(null);
                jobBean.setPaused(null);
            }
        }

        return jobBean;
    }


}
