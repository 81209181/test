package com.hkt.btu.common.core.service;


import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.bean.BtuCronJobInstBean;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;

public interface BtuSchedulerService {
    List<BtuCronJobInstBean> getAllCronJobInstance() throws SchedulerException;
    List<BtuCronJobInstBean> getAllReportJobInstance() throws SchedulerException;

    Trigger createCronTrigger(String triggerName, Date startTime, String cronExpression);
    JobDetail createJob(Class<? extends QuartzJobBean> jobClass, ApplicationContext context, String jobName, String jobGroup);

    JobKey getJobKey(String keyGroup, String keyName) throws SchedulerException;
    JobDetail getJobDetail(JobKey jobKey) throws SchedulerException;

    void resumeJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;
    void pauseJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;
    void triggerJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;
    void destroyJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;

    void rescheduleAllCronJobs();
    void scheduleJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException, ClassNotFoundException;
    void scheduleReportJob(String reportName) throws SchedulerException, InvalidInputException, ClassNotFoundException;
}
