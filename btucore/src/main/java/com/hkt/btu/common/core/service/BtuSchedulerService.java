package com.hkt.btu.common.core.service;


import com.hkt.btu.common.core.exception.InvalidInputException;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public interface BtuSchedulerService {
    void rescheduleAllCronJobs();

    Trigger createCronTrigger(String triggerName, Date startTime, String cronExpression);

    JobDetail createJob(Class<? extends QuartzJobBean> jobClass, ApplicationContext context, String jobName, String jobGroup);

    void resumeJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;

    JobDetail getJobDetail(JobKey jobKey) throws SchedulerException;

    JobKey getJobKey(String keyGroup, String keyName) throws SchedulerException;

    void pauseJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;
    void triggerJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;
    void destroyJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;

    void scheduleJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException, ClassNotFoundException;

    void scheduleReportJob(String reportName) throws SchedulerException, InvalidInputException, ClassNotFoundException;
}
