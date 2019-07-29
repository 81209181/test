package com.hkt.btu.noc.core.job;

import com.hkt.btu.noc.core.service.NocAccessRequestService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

@SuppressWarnings("WeakerAccess")
@DisallowConcurrentExecution
public class NocAutoCpAccessRequestJob extends QuartzJobBean {

    @Resource(name = "accessRequestService")
    NocAccessRequestService nocAccessRequestService;


    @Override
    @SuppressWarnings("RedundantThrows")
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        nocAccessRequestService.completeExpiredAccessRequest();
    }

}
