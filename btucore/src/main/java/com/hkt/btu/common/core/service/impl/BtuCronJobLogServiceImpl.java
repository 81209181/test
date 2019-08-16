package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.service.BtuCronJobLogService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDetail;

public class BtuCronJobLogServiceImpl implements BtuCronJobLogService {
    public static final Logger LOG = LogManager.getLogger(BtuCronJobLogServiceImpl.class);

    @Override
    public void logUserActivateJob(String jobGroup, String jobName) {
        LOG.info("log user active job:{},{}",jobGroup,jobName);
    }

    @Override
    public void logUserDeactivateJob(String jobGroup, String jobName) {
        LOG.info("log user deactivate job :{},{}",jobGroup,jobName);
    }

    @Override
    public void logUserPauseJob(JobDetail jobDetail) {
        LOG.warn("log user pause job:{}",jobDetail.getDescription());
    }

    @Override
    public void logUserResumeJob(JobDetail jobDetail) {
        LOG.info("log user resume job :{}",jobDetail.getDescription());
    }

    @Override
    public void logUserTriggerJob(JobDetail jobDetail) {
        LOG.info("log user trigger job:{}",jobDetail.getDescription());
    }

    @Override
    public void logSkip(JobDetail jobDetail) {
        LOG.info("log skip:{}",jobDetail.getDescription());
    }

    @Override
    public void logComplete(JobDetail jobDetail) {
        LOG.info("log complete :{}",jobDetail.getDescription());
    }

    @Override
    public void logError(JobDetail jobDetail) {
        LOG.info("log error:{}",jobDetail.getDescription());
    }
}
