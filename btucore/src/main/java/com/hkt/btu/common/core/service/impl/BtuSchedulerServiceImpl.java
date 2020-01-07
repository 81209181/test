package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.job.BtuSampleJob;
import com.hkt.btu.common.core.service.BtuCronJobLogService;
import com.hkt.btu.common.core.service.BtuCronJobProfileService;
import com.hkt.btu.common.core.service.BtuSchedulerService;
import com.hkt.btu.common.core.service.BtuSqlReportProfileService;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import com.hkt.btu.common.core.service.bean.BtuReportMetaDataBean;
import com.hkt.btu.common.core.service.bean.BtuSqlReportBean;
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
import java.util.List;
import java.util.stream.Collectors;

import static com.hkt.btu.common.core.service.bean.BtuSqlReportBean.REPORT_FOLDER_PATH;

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
    @Resource(name = "sqlReportProfileService")
    BtuSqlReportProfileService sqlReportProfileService;

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
        List<BtuCronJobProfileBean> jobProfileBeanList = btuCronJobProfileService.getAll();

        // find all to-run sql report job from db
        List<BtuSqlReportBean> sqlReportBeanList = sqlReportProfileService.getAllReportData(null);

        if (CollectionUtils.isEmpty(jobProfileBeanList) && CollectionUtils.isEmpty(sqlReportBeanList)) {
            LOG.info("No job to scheduled according to database.");
            return;
        }

        LOG.info("Retrieved " + jobProfileBeanList.size() + " job profile(s) from database.");

        // try schedule all job
        int successCounter = 0;
        int size = 0;
        if (CollectionUtils.isNotEmpty(jobProfileBeanList)) {
            for (BtuCronJobProfileBean jobProfileBean : jobProfileBeanList) {
                try {
                    scheduleJob(jobProfileBean);
                    successCounter++;
                } catch (SchedulerException | ClassNotFoundException | ClassCastException | InvalidInputException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            size += jobProfileBeanList.size();
        }

        if (CollectionUtils.isNotEmpty(sqlReportBeanList)) {
            for (BtuSqlReportBean sqlReportBean : sqlReportBeanList) {
                try {
                    scheduleReportJob(sqlReportBean);
                    successCounter++;
                } catch (SchedulerException | ClassNotFoundException | ClassCastException | InvalidInputException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            size += sqlReportBeanList.size();
        }

        LOG.info(String.format("Scheduled %d jobs out of %d job profiles.", successCounter, size));
    }

    @Override
    public void scheduleJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException, ClassNotFoundException {
        BtuCronJobProfileBean jobProfileBean = btuCronJobProfileService.getProfileBeanByGrpAndName(keyGroup, keyName);
        scheduleJob(jobProfileBean);
    }

    @Override
    public void scheduleReportJob(String reportId) throws SchedulerException, InvalidInputException, ClassNotFoundException {
        BtuSqlReportBean reportProfileBean = sqlReportProfileService.getProfileBeanByGrpAndName(reportId);
        scheduleReportJob(reportProfileBean);
    }

    private void scheduleReportJob(BtuSqlReportBean sqlReportBean) throws ClassNotFoundException, SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        // check already existed
        Class<? extends QuartzJobBean> jobClass = Class.forName(sqlReportBean.getJobClass()).asSubclass(QuartzJobBean.class);

        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(sqlReportBean.getReportName(), BtuSqlReportBean.KEY_GROUP)
                .build();
        JobKey jobKey = jobDetail.getKey();
        if (scheduler.checkExists(jobKey)) {
            LOG.warn("Already scheduled Job: " + jobKey);
            return;
        }

        // check cron expression
        if (!CronExpression.isValidExpression(sqlReportBean.getCronExp())) {
            LOG.error(jobKey + " has invalid cron expression: " + sqlReportBean.getCronExp());
            throw new InvalidInputException("Invalid cron expression");
        }

        // calculate start time
        LocalDateTime nowPlusTenSecond = LocalDateTime.now().plusSeconds(10);
        Date startTime = Date.from(nowPlusTenSecond.atZone(ZoneId.systemDefault()).toInstant());

        // schedule job
        jobDetail = createReportJob(jobClass, context, sqlReportBean);
        Trigger trigger = createCronTrigger(sqlReportBean.getReportName(), startTime,
                sqlReportBean.getCronExp());

        scheduler.scheduleJob(jobDetail, trigger);
        if (sqlReportProfileService.isRunnable(sqlReportBean)) {
            LOG.info("Scheduled job: " + jobKey);
        } else {
            scheduler.pauseJob(jobKey);
            LOG.info("Scheduled job (paused on this host): " + jobKey);
        }

    }


    private void scheduleJob(BtuCronJobProfileBean jobProfileBean) throws ClassNotFoundException, SchedulerException {
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
        jobDetail = createJob(jobClass, context, jobKey.getName(), jobKey.getGroup());
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

    /**
     * Create Quartz Job.
     *
     * @param jobClass Class whose executeInternal() method needs to be called.
     * @param context  Spring application context.
     * @param jobName  Job name.
     * @param jobGroup Job group.
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

    public JobDetail createReportJob(Class<? extends QuartzJobBean> jobClass, ApplicationContext context, BtuSqlReportBean sqlReportBean) {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(false);
        factoryBean.setApplicationContext(context);
        factoryBean.setName(sqlReportBean.getReportName());
        factoryBean.setGroup(BtuSqlReportBean.KEY_GROUP);

        // Build Report Meta Data
        BtuReportMetaDataBean metaData = new BtuReportMetaDataBean();
        metaData.setSql(sqlReportBean.getSql());
        metaData.setExportTo(REPORT_FOLDER_PATH);
        metaData.setEmailTo(sqlReportBean.getEmailTo());
        metaData.setReportName(sqlReportBean.getReportName());

        // set job data map
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(sqlReportBean.getReportName() + BtuSqlReportBean.KEY_GROUP, jobClass.getName());
        jobDataMap.put(BtuSqlReportBean.REPORT_JOBDATAMAP_KEY, metaData);
        factoryBean.setJobDataMap(jobDataMap);
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
        LOG.info("Destroyed job: " + targetJobKey);
    }
}
