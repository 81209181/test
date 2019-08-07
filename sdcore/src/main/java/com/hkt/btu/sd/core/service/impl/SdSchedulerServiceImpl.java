package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.impl.BtuSchedulerServiceImpl;
import com.hkt.btu.sd.core.service.SdCronJobProfileService;
import com.hkt.btu.sd.core.service.SdSchedulerService;
import com.hkt.btu.sd.core.service.bean.SdCronJobInstBean;
import com.hkt.btu.sd.core.service.bean.SdCronJobProfileBean;
import com.hkt.btu.sd.core.service.populator.SdCronJobInstBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class SdSchedulerServiceImpl extends BtuSchedulerServiceImpl implements SdSchedulerService {
    private static final Logger LOG = LogManager.getLogger(SdSchedulerServiceImpl.class);


    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    @Autowired
    private ApplicationContext context;

    @Resource(name = "cronJobProfileService")
    SdCronJobProfileService sdCronJobProfileService;

    @Resource(name = "cronJobInstBeanPopulator")
    SdCronJobInstBeanPopulator sdCronJobInstBeanPopulator;



    @Override
    public void rescheduleAllCronJobs() {
        // remove all existing schedule job
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    scheduler.deleteJob(jobKey);
                }
            }

        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
            return;
        }
        LOG.info("Removed all existing scheduled jobs.");

        // find all to-run schedule job from db
        List<SdCronJobProfileBean> jobProfileBeanList = sdCronJobProfileService.getAll();
        if(CollectionUtils.isEmpty(jobProfileBeanList)){
            LOG.info("No job to scheduled according to database.");
            return;
        }
        LOG.info("Retrieved " + jobProfileBeanList.size() +" job profile(s) from database.");

        // try schedule all job
        int successCounter = 0;
        for(SdCronJobProfileBean jobProfileBean : jobProfileBeanList){
            try {
                scheduleJob(jobProfileBean);
                successCounter++;
            } catch (SchedulerException | ClassNotFoundException | ClassCastException | InvalidInputException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        LOG.info( String.format("Scheduled %d jobs out of %d job profiles.", successCounter, jobProfileBeanList.size()) );
    }



    @Override
    public List<SdCronJobInstBean> getAllCronJobInstance() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        List<SdCronJobInstBean> jobBeanList = new LinkedList<>();
        for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                SdCronJobInstBean jobBean = getCronJobInstance(jobKey);
                jobBeanList.add(jobBean);
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
        if ( CollectionUtils.isEmpty(triggerList) ) {
            jobBean.setPaused(true);
        } else {
            List<CronTrigger> cronTriggerList = new LinkedList<>();
            for(Trigger trigger : triggerList){
                if(trigger instanceof CronTrigger){
                    cronTriggerList.add((CronTrigger) trigger);
                }
            }

            if ( CollectionUtils.size(cronTriggerList)==1 ) {
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
    @Override
    public void scheduleJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException, ClassNotFoundException {
        SdCronJobProfileBean jobProfileBean = sdCronJobProfileService.getProfileBeanByGrpAndName(keyGroup, keyName);
        scheduleJob(jobProfileBean);
    }

    private void scheduleJob(SdCronJobProfileBean jobProfileBean) throws SchedulerException, InvalidInputException, ClassNotFoundException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        // check already existed
        Class<? extends QuartzJobBean> jobClass = Class.forName(jobProfileBean.getJobClass()).asSubclass(QuartzJobBean.class);
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobProfileBean.getKeyName(), jobProfileBean.getKeyGroup()).build();
        JobKey jobKey = jobDetail.getKey();
        if( scheduler.checkExists(jobKey) ){
            LOG.warn( "Already scheduled Job: " + jobKey);
            return;
        }

        // check cron expression
        if( ! CronExpression.isValidExpression(jobProfileBean.getCronExp()) ){
            LOG.error( jobKey + " has invalid cron expression: " + jobProfileBean.getCronExp() );
            throw new InvalidInputException("Invalid cron expression");
        }

        // calculate start time
        LocalDateTime nowPlusTenSecond = LocalDateTime.now().plusSeconds(10);
        Date startTime = Date.from(nowPlusTenSecond.atZone(ZoneId.systemDefault()).toInstant());

        // schedule job
        jobDetail = createJob(jobClass, context, jobKey.getName(), jobKey.getGroup());
        Trigger trigger = createCronTrigger(jobProfileBean.getKeyName(), startTime,
                jobProfileBean.getCronExp());

        scheduler.scheduleJob(jobDetail, trigger);
        if( sdCronJobProfileService.isRunnable(jobProfileBean) ){
            LOG.info("Scheduled job: " + jobKey);
        }else {
            scheduler.pauseJob(jobKey);
            LOG.info("Scheduled job (paused on this host): " + jobKey);
        }
    }




}
