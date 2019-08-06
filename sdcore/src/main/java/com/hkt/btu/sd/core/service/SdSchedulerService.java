package com.hkt.btu.sd.core.service;


import com.hkt.btu.sd.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.bean.SdCronJobInstBean;
import org.quartz.SchedulerException;

import java.util.List;

public interface SdSchedulerService {

    void rescheduleAllCronJobs();

    List<SdCronJobInstBean> getAllCronJobInstance() throws SchedulerException;

    void resumeJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;
    void pauseJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;
    void triggerJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;


    void scheduleJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException, ClassNotFoundException;
    void destroyJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;
}
