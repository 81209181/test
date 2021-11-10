package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.BtuCronJobLogService;
import com.hkt.btu.common.core.service.BtuCronJobProfileService;
import com.hkt.btu.common.core.service.BtuSchedulerService;
import com.hkt.btu.common.core.service.bean.BtuCronJobInstBean;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import com.hkt.btu.common.core.service.populator.BtuCronJobInstBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class BtuSchedulerServiceImpl implements BtuSchedulerService {
    private static final Logger LOG = LogManager.getLogger(BtuSchedulerServiceImpl.class);

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    @Autowired
    private ApplicationContext context;

    @Resource(name = "cronJobLogService")
    BtuCronJobLogService btuCronJobLogService;
    @Resource(name = "cronJobProfileService")
    BtuCronJobProfileService btuCronJobProfileService;

    @Resource(name = "cronJobInstBeanPopulator")
    BtuCronJobInstBeanPopulator cronJobInstBeanPopulator;

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
        List<BtuCronJobProfileBean> jobProfileBeanList = btuCronJobProfileService.getAllJobProfile();

        LOG.info("Retrieved " + jobProfileBeanList.size() + " job profile(s) from database.");

        // try schedule all job
        int successCounter = 0;
        int totalCount = 0;

        if (CollectionUtils.isNotEmpty(jobProfileBeanList)) {
            for (BtuCronJobProfileBean jobProfileBean : jobProfileBeanList) {
                try {
                    scheduleJob(jobProfileBean);
                    successCounter++;
                } catch (SchedulerException | ClassNotFoundException | ClassCastException | InvalidInputException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            totalCount += jobProfileBeanList.size();
        }

        LOG.info(String.format("Scheduled %d jobs out of %d job profiles.", successCounter, totalCount));
    }


    public void scheduleJob(BtuCronJobProfileBean jobProfileBean) throws ClassNotFoundException, SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        // check already existed
        Class<? extends QuartzJobBean> jobClass = Class.forName(jobProfileBean.getJobClass()).asSubclass(QuartzJobBean.class);
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobProfileBean.getKeyName(), jobProfileBean.getKeyGroup()).build();
        JobKey jobKey = jobDetail.getKey();
        if (scheduler.checkExists(jobKey)) {
            LOG.warn("Already scheduled Job: " + jobKey);
            return;
        }

        // check cron expression
        if (!CronExpression.isValidExpression(jobProfileBean.getCronExp())) {
            LOG.error(jobKey + " has invalid cron expression: " + jobProfileBean.getCronExp());
            throw new InvalidInputException("Invalid cron expression");
        }

        // calculate start time
        LocalDateTime nowPlusTenSecond = LocalDateTime.now().plusSeconds(10);
        Date startTime = Date.from(nowPlusTenSecond.atZone(ZoneId.systemDefault()).toInstant());

        // schedule job
        jobDetail = createJob(jobClass, context, jobKey.getName(), jobKey.getGroup(), jobProfileBean);
        Trigger trigger = createCronTrigger(jobProfileBean.getKeyName(), startTime,
                jobProfileBean.getCronExp());

        scheduler.scheduleJob(jobDetail, trigger);
        if (btuCronJobProfileService.isRunnable(jobProfileBean)) {
            LOG.info("Scheduled job: " + jobKey);
        } else {
            scheduler.pauseJob(jobKey);
            LOG.info("Scheduled job (paused on this host): " + jobKey);
        }

    }

    /**
     * Create cron trigger.
     *
     * @param triggerName    Trigger name.
     * @param startTime      Trigger start time.
     * @param cronExpression Cron expression.
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


    private JobDetail createJob(Class<? extends QuartzJobBean> jobClass, ApplicationContext context,
                               String jobName, String jobGroup, BtuCronJobProfileBean jobProfileBean) {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(false);
        factoryBean.setApplicationContext(context);
        factoryBean.setName(jobName);
        factoryBean.setGroup(jobGroup);

        factoryBean.setJobDataMap(jobProfileBean.getJobDataMap());
        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }


    @Override
    public void resumeJob(String keyGroup, String keyName) throws SchedulerException {
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
        if (jobDetail == null) {
            throw new SchedulerException("Job detail not found (" + jobKey + ").");
        }
        return jobDetail;
    }

    @Override
    public JobKey getJobKey(String keyGroup, String keyName) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                if (StringUtils.equals(jobKey.getGroup(), keyGroup)
                        && StringUtils.equals(jobKey.getName(), keyName)) {
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
        TriggerKey triggerKey = TriggerKey.triggerKey(keyName);
        scheduler.pauseTrigger(triggerKey);
        scheduler.unscheduleJob(triggerKey);
        scheduler.deleteJob(targetJobKey);
        LOG.info("Destroyed job: {}", targetJobKey);
    }

    @Override
    public List<BtuCronJobInstBean> getAllCronJobInstance() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        List<BtuCronJobInstBean> jobBeanList = new LinkedList<>();
        for (String groupName : scheduler.getJobGroupNames()) {

            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                BtuCronJobInstBean jobBean = getCronJobInstance(jobKey);
                jobBeanList.add(jobBean);
            }

        }
        return jobBeanList;
    }

    @Override
    public List<BtuCronJobInstBean> getAllReportJobInstance() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        List<BtuCronJobInstBean> jobBeanList = new LinkedList<>();
        return jobBeanList;
    }

    private BtuCronJobInstBean getCronJobInstance(JobKey jobKey) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        BtuCronJobInstBean jobBean = new BtuCronJobInstBean();

        JobDetail jobDetail = getJobDetail(jobKey);
        cronJobInstBeanPopulator.populate(jobDetail, jobBean);

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
                cronJobInstBeanPopulator.populate(cronTrigger, jobBean);

                Trigger.TriggerState triggerState = scheduler.getTriggerState(cronTrigger.getKey());
                cronJobInstBeanPopulator.populate(triggerState, jobBean);
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
