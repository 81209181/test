package com.hkt.btu.common.core.service;


import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.bean.BtuCronJobInstBean;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import java.util.Date;
import java.util.List;

public interface BtuSchedulerService {
    List<BtuCronJobInstBean> getAllCronJobInstance() throws SchedulerException;
    List<BtuCronJobInstBean> getAllReportJobInstance() throws SchedulerException;

    Trigger createCronTrigger(String triggerName, Date startTime, String cronExpression);

    JobKey getJobKey(String keyGroup, String keyName) throws SchedulerException;
    JobDetail getJobDetail(JobKey jobKey) throws SchedulerException;

    void resumeJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;
    void pauseJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;
    void triggerJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;
    void destroyJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;

    void rescheduleAllCronJobs();
    void scheduleJob(BtuCronJobProfileBean cronJobProfileBean) throws SchedulerException, InvalidInputException, ClassNotFoundException;
}
