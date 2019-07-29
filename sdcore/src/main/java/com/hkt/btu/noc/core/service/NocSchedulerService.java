package com.hkt.btu.noc.core.service;


import com.hkt.btu.noc.core.exception.InvalidInputException;
import com.hkt.btu.noc.core.service.bean.NocCronJobInstBean;
import org.quartz.SchedulerException;

import java.util.List;

public interface NocSchedulerService {

    void rescheduleAllCronJobs();

    List<NocCronJobInstBean> getAllCronJobInstance() throws SchedulerException;

    void resumeJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;
    void pauseJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;
    void triggerJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;


    void scheduleJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException, ClassNotFoundException;
    void destroyJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException;
}
