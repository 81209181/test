package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.dao.entity.BtuCronJobLogEntity;
import com.hkt.btu.common.core.dao.entity.BtuUserEntity;
import com.hkt.btu.common.core.exception.BtuMissingImplException;
import com.hkt.btu.common.core.service.BtuCronJobLogService;
import com.hkt.btu.common.core.service.BtuCronJobProfileService;
import com.hkt.btu.common.core.service.BtuUserService;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDetail;

import javax.annotation.Resource;

public class BtuCronJobLogServiceImpl implements BtuCronJobLogService {
    public static final Logger LOG = LogManager.getLogger(BtuCronJobLogServiceImpl.class);

    @Resource(name = "userService")
    BtuUserService userService;
    @Resource(name = "cronJobProfileService")
    BtuCronJobProfileService cronJobProfileService;


    protected void addJobLogInternal(String jobGroup, String jobName, String jobClass, String createby, String action) {
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
    }

    @Override
    public void logUserActivateJob(String jobGroup, String jobName) {
        BtuCronJobProfileBean profileBean = cronJobProfileService.getProfileBeanByGrpAndName(jobGroup, jobName);
        String jobClass = profileBean.getJobClass();

        LOG.info("Activate job: {}, {}", jobGroup, jobName);
        String userId = userService.getCurrentUserId();
        addJobLogInternal(jobGroup, jobName, jobClass, userId, BtuCronJobLogEntity.ACTION.ACTIVATE);
    }

    @Override
    public void logUserDeactivateJob(String jobGroup, String jobName) {
        BtuCronJobProfileBean profileBean = cronJobProfileService.getProfileBeanByGrpAndName(jobGroup, jobName);
        String jobClass = profileBean.getJobClass();

        LOG.info("Deactivate job: {}, {}", jobGroup, jobName);
        String userId = userService.getCurrentUserId();
        addJobLogInternal(jobGroup, jobName, jobClass, userId, BtuCronJobLogEntity.ACTION.DEACTIVATE);
    }

    @Override
    public void logUserPauseJob(JobDetail jobDetail) {
        String jobGroup = jobDetail.getKey().getGroup();
        String jobName = jobDetail.getKey().getName();
        String jobClass = jobDetail.getJobClass().getName();

        LOG.warn("Pause job: {}, {}", jobGroup, jobName);
        String userId = userService.getCurrentUserId();
        addJobLogInternal(jobGroup, jobName, jobClass, userId, BtuCronJobLogEntity.ACTION.PAUSE);
    }

    @Override
    public void logUserResumeJob(JobDetail jobDetail) {
        String jobGroup = jobDetail.getKey().getGroup();
        String jobName = jobDetail.getKey().getName();
        String jobClass = jobDetail.getJobClass().getName();

        LOG.info("Resume job: {}, {}", jobGroup, jobName);
        String userId = userService.getCurrentUserId();
        addJobLogInternal(jobGroup, jobName, jobClass, userId, BtuCronJobLogEntity.ACTION.RESUME);
    }

    @Override
    public void logUserTriggerJob(JobDetail jobDetail) {
        String jobGroup = jobDetail.getKey().getGroup();
        String jobName = jobDetail.getKey().getName();
        String jobClass = jobDetail.getJobClass().getName();

        LOG.info("Trigger job: {}, {}", jobGroup, jobName);
        String userId = userService.getCurrentUserId();
        addJobLogInternal(jobGroup, jobName, jobClass, userId, BtuCronJobLogEntity.ACTION.TRIGGER);

    }

    @Override
    public void logSkip(JobDetail jobDetail) {
        String jobGroup = jobDetail.getKey().getGroup();
        String jobName = jobDetail.getKey().getName();
        String jobClass = jobDetail.getJobClass().getName();

        LOG.info("Skip job: {}, {}", jobGroup, jobName);
        addJobLogInternal(jobGroup, jobName, jobClass, BtuUserEntity.SYSTEM.USER_ID, BtuCronJobLogEntity.ACTION.SKIP);
    }

    @Override
    public void logComplete(JobDetail jobDetail) {
        String jobGroup = jobDetail.getKey().getGroup();
        String jobName = jobDetail.getKey().getName();
        String jobClass = jobDetail.getJobClass().getName();

        LOG.info("Complete job: {}, {}", jobGroup, jobName);
        addJobLogInternal(jobGroup, jobName, jobClass, BtuUserEntity.SYSTEM.USER_ID, BtuCronJobLogEntity.ACTION.COMPLETE);
    }

    @Override
    public void logError(JobDetail jobDetail) {
        String jobGroup = jobDetail.getKey().getGroup();
        String jobName = jobDetail.getKey().getName();
        String jobClass = jobDetail.getJobClass().getName();

        LOG.warn("Error in job: {}, {}", jobGroup, jobName);
        addJobLogInternal(jobGroup, jobName, jobClass, BtuUserEntity.SYSTEM.USER_ID, BtuCronJobLogEntity.ACTION.ERROR);
    }
}
