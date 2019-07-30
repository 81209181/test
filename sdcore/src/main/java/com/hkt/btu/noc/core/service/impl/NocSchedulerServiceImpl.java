package com.hkt.btu.noc.core.service.impl;

import com.hkt.btu.noc.core.exception.InvalidInputException;
import com.hkt.btu.noc.core.service.NocCronJobLogService;
import com.hkt.btu.noc.core.service.NocCronJobProfileService;
import com.hkt.btu.noc.core.service.NocSchedulerService;
import com.hkt.btu.noc.core.service.bean.NocCronJobInstBean;
import com.hkt.btu.noc.core.service.bean.NocCronJobProfileBean;
import com.hkt.btu.noc.core.service.populator.NocCronJobInstBeanPopulator;
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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class NocSchedulerServiceImpl implements NocSchedulerService {
    private static final Logger LOG = LogManager.getLogger(NocSchedulerServiceImpl.class);


    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    @Autowired
    private ApplicationContext context;

//    @Resource(name = "cronJobProfileService")
    @Autowired
    NocCronJobProfileService nocCronJobProfileService;
    @Autowired
    NocCronJobLogService nocCronJobLogService;

//    @Resource(name = "cronJobInstBeanPopulator")
    @Autowired
    NocCronJobInstBeanPopulator nocCronJobInstBeanPopulator;



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
        List<NocCronJobProfileBean> jobProfileBeanList = nocCronJobProfileService.getAll();
        if(CollectionUtils.isEmpty(jobProfileBeanList)){
            LOG.info("No job to scheduled according to database.");
            return;
        }
        LOG.info("Retrieved " + jobProfileBeanList.size() +" job profile(s) from database.");

        // try schedule all job
        int successCounter = 0;
        for(NocCronJobProfileBean jobProfileBean : jobProfileBeanList){
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
    public List<NocCronJobInstBean> getAllCronJobInstance() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        List<NocCronJobInstBean> jobBeanList = new LinkedList<>();
        for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                NocCronJobInstBean jobBean = getCronJobInstance(jobKey);
                jobBeanList.add(jobBean);
            }
        }

        return jobBeanList;
    }

    private NocCronJobInstBean getCronJobInstance(JobKey jobKey) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        NocCronJobInstBean jobBean = new NocCronJobInstBean();

        JobDetail jobDetail = getJobDetail(jobKey);
        nocCronJobInstBeanPopulator.populate(jobDetail, jobBean);

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
                nocCronJobInstBeanPopulator.populate(cronTrigger, jobBean);

                Trigger.TriggerState triggerState = scheduler.getTriggerState(cronTrigger.getKey());
                nocCronJobInstBeanPopulator.populate(triggerState, jobBean);
            } else {
                jobBean.setCurrentCronExp("Unsupported for multi-cron-trigger job.");
                jobBean.setLastRunTime(null);
                jobBean.setNextRunTime(null);
                jobBean.setPaused(null);
            }
        }

        return jobBean;
    }

    private JobKey getJobKey(String keyGroup, String keyName) throws SchedulerException, InvalidInputException {
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

    private JobDetail getJobDetail(JobKey jobKey) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if(jobDetail==null){
            throw new SchedulerException("Job detail not found (" + jobKey + ").");
        }
        return jobDetail;
    }

    @Override
    public void resumeJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException {
        JobKey targetJobKey = getJobKey(keyGroup, keyName);
        JobDetail jobDetail = getJobDetail(targetJobKey);

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.resumeJob(targetJobKey);

        LOG.info("Resumed job: " + targetJobKey);
        nocCronJobLogService.logUserResumeJob(jobDetail);
    }

    @Override
    public void pauseJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException {
        JobKey targetJobKey = getJobKey(keyGroup, keyName);
        JobDetail jobDetail = getJobDetail(targetJobKey);

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.pauseJob(targetJobKey);

        LOG.info("Paused job: " + targetJobKey);
        nocCronJobLogService.logUserPauseJob(jobDetail);
    }

    @Override
    public void triggerJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException {
        JobKey targetJobKey = getJobKey(keyGroup, keyName);
        JobDetail jobDetail = getJobDetail(targetJobKey);

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.triggerJob(targetJobKey);

        LOG.info("Triggered job: " + targetJobKey);
        nocCronJobLogService.logUserTriggerJob(jobDetail);
    }

    @Override
    public void scheduleJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException, ClassNotFoundException {
        NocCronJobProfileBean jobProfileBean = nocCronJobProfileService.getProfileBeanByGrpAndName(keyGroup, keyName);
        scheduleJob(jobProfileBean);
    }

    @Override
    public void destroyJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException {
        JobKey targetJobKey = getJobKey(keyGroup, keyName);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.deleteJob(targetJobKey);
        LOG.info("Destroyed job: " + targetJobKey);
    }

    private void scheduleJob(NocCronJobProfileBean jobProfileBean) throws SchedulerException, InvalidInputException, ClassNotFoundException {
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
        if( nocCronJobProfileService.isRunnable(jobProfileBean) ){
            LOG.info("Scheduled job: " + jobKey);
        }else {
            scheduler.pauseJob(jobKey);
            LOG.info("Scheduled job (paused on this host): " + jobKey);
        }
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
    private JobDetail createJob(Class<? extends QuartzJobBean> jobClass,
                                ApplicationContext context, String jobName, String jobGroup) {
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

    /**
     * Create cron trigger.
     *
     * @param triggerName        Trigger name.
     * @param startTime          Trigger start time.
     * @param cronExpression     Cron expression.
     * @return {@link CronTrigger}
     */
    private CronTrigger createCronTrigger(String triggerName, Date startTime, String cronExpression) {
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



}
