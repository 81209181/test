package com.hkt.btu.sd.core.job;

import com.hkt.btu.sd.core.service.SdSiteConfigService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

@DisallowConcurrentExecution
public class SdSiteConfigReloadJob extends QuartzJobBean {

    @Resource(name = "siteConfigService")
    SdSiteConfigService siteConfigService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        siteConfigService.reload();
    }
}
