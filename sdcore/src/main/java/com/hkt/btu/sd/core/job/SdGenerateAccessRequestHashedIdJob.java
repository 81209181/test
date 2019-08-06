package com.hkt.btu.sd.core.job;

import com.hkt.btu.sd.core.service.SdAccessRequestHashService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

@SuppressWarnings("WeakerAccess")
@DisallowConcurrentExecution
public class SdGenerateAccessRequestHashedIdJob extends QuartzJobBean {

    @Resource(name = "accessRequestHashService")
    SdAccessRequestHashService sdAccessRequestHashService;


    @Override
    @SuppressWarnings("RedundantThrows")
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        sdAccessRequestHashService.generateHashId();
    }

}
