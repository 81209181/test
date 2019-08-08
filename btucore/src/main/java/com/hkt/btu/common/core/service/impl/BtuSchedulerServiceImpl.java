package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.BtuCronJobLogService;
import com.hkt.btu.common.core.service.BtuSchedulerService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;

public class BtuSchedulerServiceImpl implements BtuSchedulerService {
    private static final Logger LOG = LogManager.getLogger(BtuSchedulerServiceImpl.class);

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    @Autowired
    private ApplicationContext context;
    @Resource(name = "cronJobLogService")
    BtuCronJobLogService btuCronJobLogService;

    @Override
    public void rescheduleAllCronJobs() {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
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
        // try schedule all job
    }

    /**
     * Create cron trigger.
     *
     * @param triggerName        Trigger name.
     * @param startTime          Trigger start time.
     * @param cronExpression     Cron expression.
     * @return {@link CronTrigger}
     */
    @Override
    public Trigger createCronTrigger(String triggerName, Date startTime, String cronExpression) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setName(triggerName);
        factoryBean.setStartTime(startTime);
        factoryBean.setCronExpression(cronExpression);
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        try {
            factoryBean.afterPropertiesSet();
        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
        }
        return factoryBean.getObject();
    }
    /**
     * Create Quartz Job.
     *
     * @param jobClass  Class whose executeInternal() method needs to be called.
     * @param context   Spring application context.
     * @param jobName   Job name.
     * @param jobGroup  Job group.
     * @return JobDetail object
     */
    @Override
    public JobDetail createJob(Class<? extends QuartzJobBean> jobClass, ApplicationContext context, String jobName, String jobGroup) {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(false);
        factoryBean.setApplicationContext(context);
        factoryBean.setName(jobName);
        factoryBean.setGroup(jobGroup);

        // set job data map
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(jobName + jobGroup, jobClass.getName());
        factoryBean.setJobDataMap(jobDataMap);
        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();

    }

    @Override
    public void resumeJob(String keyGroup, String keyName) throws SchedulerException{
        JobKey targetJobKey = getJobKey(keyGroup, keyName);
        JobDetail jobDetail = getJobDetail(targetJobKey);

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.resumeJob(targetJobKey);

        LOG.info("Resumed job: " + targetJobKey);
        btuCronJobLogService.logUserResumeJob(jobDetail);

    }

    @Override
    public JobDetail getJobDetail(JobKey jobKey) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if(jobDetail==null){
            throw new SchedulerException("Job detail not found (" + jobKey + ").");
        }
        return jobDetail;
    }

    @Override
    public JobKey getJobKey(String keyGroup, String keyName) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                if( StringUtils.equals(jobKey.getGroup(), keyGroup)
                        && StringUtils.equals(jobKey.getName(), keyName) ){
                    return jobKey;
                }
            }
        }
        throw new InvalidInputException("Invalid job key (" + keyGroup + ", " + keyName + ").");
    }

    @Override
    public void pauseJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException {
        JobKey targetJobKey = getJobKey(keyGroup, keyName);
        JobDetail jobDetail = getJobDetail(targetJobKey);

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.pauseJob(targetJobKey);

        LOG.info("Paused job: " + targetJobKey);
        btuCronJobLogService.logUserPauseJob(jobDetail);
    }

    @Override
    public void triggerJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException {
        JobKey targetJobKey = getJobKey(keyGroup, keyName);
        JobDetail jobDetail = getJobDetail(targetJobKey);

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.triggerJob(targetJobKey);

        LOG.info("Triggered job: " + targetJobKey);
        btuCronJobLogService.logUserTriggerJob(jobDetail);

    }

    @Override
    public void destroyJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException {
        JobKey targetJobKey = getJobKey(keyGroup, keyName);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.deleteJob(targetJobKey);
        LOG.info("Destroyed job: " + targetJobKey);
    }
}
