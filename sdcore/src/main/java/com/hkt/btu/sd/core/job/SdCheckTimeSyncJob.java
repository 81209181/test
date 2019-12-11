package com.hkt.btu.sd.core.job;

import com.hkt.btu.sd.core.service.SdHealthCheckService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

@DisallowConcurrentExecution
public class SdCheckTimeSyncJob extends QuartzJobBean {

    @Resource(name = "healthCheckService")
    SdHealthCheckService sdHealthCheckService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        sdHealthCheckService.checkTimeSync();
    }
}
