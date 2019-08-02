package com.hkt.btu.common.core.service;


import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.bean.BtuCronJobInstBean;
import org.quartz.SchedulerException;

import java.util.List;

public interface BtuSchedulerService {

    void rescheduleAllCronJobs();

    List<BtuCronJobInstBean> getAllCronJobInstance() throws SchedulerException;

    void resumeJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;

    void pauseJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;

    void triggerJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;


    void scheduleJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException, ClassNotFoundException;

    void destroyJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;
}
