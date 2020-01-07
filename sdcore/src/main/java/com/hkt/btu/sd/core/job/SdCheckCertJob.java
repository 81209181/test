package com.hkt.btu.sd.core.job;

import com.hkt.btu.common.core.service.BtuSiteConfigService;
import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import com.hkt.btu.sd.core.service.SdCertService;
import com.hkt.btu.sd.core.service.SdEmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import javax.mail.MessagingException;

@DisallowConcurrentExecution
public class SdCheckCertJob extends QuartzJobBean {
    private static final Logger LOG = LogManager.getLogger(SdCheckCertJob.class);

    @Resource(name = "siteConfigService")
    BtuSiteConfigService siteConfigService;
    @Resource(name = "certService")
    SdCertService certService;
    @Resource(name = "emailService")
    SdEmailService emailService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String appHttpUrl = siteConfigService.getSiteConfigBean().getAppHttpsUrl();
        try {
            certService.checkCert(appHttpUrl);
        } catch (RuntimeException e) {
            try {
                emailService.send(BtuSiteConfigBean.DEFAULT_MAIL_USERNAME,"SdCheckCertJob report",e.getMessage());
            } catch (MessagingException ex) {
                LOG.warn(ex);
            }
        }
    }
}
