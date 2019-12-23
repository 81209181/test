package com.hkt.btu.sd.core.job;

import com.hkt.btu.sd.core.service.SdPublicHolidayService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

@DisallowConcurrentExecution
public class SdCheckPublicHolidayJob extends QuartzJobBean {

    @Resource(name = "publicHolidayService")
    SdPublicHolidayService sdPublicHolidayService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        sdPublicHolidayService.checkPublicHoliday();
    }
}
