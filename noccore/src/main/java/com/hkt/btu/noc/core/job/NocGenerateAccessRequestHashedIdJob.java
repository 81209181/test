package com.hkt.btu.noc.core.job;

import com.hkt.btu.noc.core.service.NocAccessRequestHashService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

@SuppressWarnings("WeakerAccess")
@DisallowConcurrentExecution
public class NocGenerateAccessRequestHashedIdJob extends QuartzJobBean {

    @Resource(name = "accessRequestHashService")
    NocAccessRequestHashService nocAccessRequestHashService;


    @Override
    @SuppressWarnings("RedundantThrows")
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        nocAccessRequestHashService.generateHashId();
    }

}
