package com.hkt.btu.sd.core.job;

import com.hkt.btu.sd.core.service.SdHistoryService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

@DisallowConcurrentExecution
public class SdCleanHistoryTableJob extends QuartzJobBean {

    @Resource(name = "historyService")
    SdHistoryService historyService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        historyService.cleanHistoryData();
    }
}
