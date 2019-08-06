package com.hkt.btu.sd.core.job;

import com.hkt.btu.sd.core.service.SdAccessRequestService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

@SuppressWarnings("WeakerAccess")
@DisallowConcurrentExecution
public class SdAutoCpAccessRequestJob extends QuartzJobBean {

    @Resource(name = "accessRequestService")
    SdAccessRequestService sdAccessRequestService;


    @Override
    @SuppressWarnings("RedundantThrows")
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        sdAccessRequestService.completeExpiredAccessRequest();
    }

}
