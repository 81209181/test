package com.hkt.btu.sd.core.job;

import com.hkt.btu.sd.core.service.SdAuditTrailService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

@DisallowConcurrentExecution
public class SdCleanAuditTableJob extends QuartzJobBean {

    @Resource(name = "auditTrailService")
    SdAuditTrailService auditTrailService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        auditTrailService.cleanAuditTrail();
    }
}
