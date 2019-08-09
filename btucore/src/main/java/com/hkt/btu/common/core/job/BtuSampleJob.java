package com.hkt.btu.common.core.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;

public class BtuSampleJob extends QuartzJobBean {
    private static final Logger LOG = LogManager.getLogger(BtuSampleJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOG.info(LocalDateTime.now());
    }
}
