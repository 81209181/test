package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.service.BtuCronJobLogService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDetail;

public class BtuCronJobLogServiceImpl implements BtuCronJobLogService {
    private static final Logger LOG = LogManager.getLogger(BtuCronJobLogServiceImpl.class);
    @Override
    public void logUserActivateJob(String jobGroup, String jobName) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
    }

    @Override
    public void logUserDeactivateJob(String jobGroup, String jobName) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
    }

    @Override
    public void logUserPauseJob(JobDetail jobDetail) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
    }

    @Override
    public void logUserResumeJob(JobDetail jobDetail) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
    }

    @Override
    public void logUserTriggerJob(JobDetail jobDetail) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
    }

    @Override
    public void logSkip(JobDetail jobDetail) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
    }

    @Override
    public void logComplete(JobDetail jobDetail) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
    }

    @Override
    public void logError(JobDetail jobDetail) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
    }
}
