package com.hkt.btu.sd.core.service;


import com.hkt.btu.common.core.service.BtuSchedulerService;
import com.hkt.btu.sd.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.bean.SdCronJobInstBean;
import org.quartz.SchedulerException;

import java.util.List;

public interface SdSchedulerService extends BtuSchedulerService {

    List<SdCronJobInstBean> getAllCronJobInstance() throws SchedulerException;

    void scheduleJob(String keyGroup, String keyName) throws SchedulerException, InvalidInputException, ClassNotFoundException;
}
