package com.hkt.btu.sd.core.job;

import com.hkt.btu.sd.core.service.SdAuditTrailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

@DisallowConcurrentExecution
public class SdCleanAuditTableJob extends QuartzJobBean {
    private static final Logger LOG = LogManager.getLogger(SdCleanAuditTableJob.class);

    @Resource(name = "auditTrailService")
    SdAuditTrailService auditTrailService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOG.info("Clean audit trail data count: {}",auditTrailService.cleanAuditTrail());
    }
}
