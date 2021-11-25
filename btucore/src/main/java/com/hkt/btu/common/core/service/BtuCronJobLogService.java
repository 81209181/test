package com.hkt.btu.common.core.service;

import org.quartz.JobDetail;

public interface BtuCronJobLogService {
    // job db status change log
    void logUserActivateJob(String jobGroup, String jobName);
    void logUserDeactivateJob(String jobGroup, String jobName);

    // job instance log
    void logUserPauseJob(JobDetail jobDetail);
    void logUserResumeJob(JobDetail jobDetail);
    void logUserTriggerJob(JobDetail jobDetail);

    void logSkip(JobDetail jobDetail);
    void logComplete(JobDetail jobDetail);
    void logError(JobDetail jobDetail);
}