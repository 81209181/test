package com.hkt.btu.sd.core.job;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.service.BtuConfigParamService;
import com.hkt.btu.common.core.service.BtuSiteConfigService;
import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;
import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import com.hkt.btu.sd.core.service.SdCertService;
import com.hkt.btu.sd.core.service.SdEmailService;
import com.hkt.btu.sd.core.service.bean.SdCheckCertBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.List;

@DisallowConcurrentExecution
public class SdCheckCertJob extends QuartzJobBean {
    private static final Logger LOG = LogManager.getLogger(SdCheckCertJob.class);

    @Resource(name = "siteConfigService")
    BtuSiteConfigService siteConfigService;
    @Resource(name = "configParamService")
    BtuConfigParamService configParamService;
    @Resource(name = "certService")
    SdCertService certService;
    @Resource(name = "emailService")
    SdEmailService emailService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<BtuConfigParamBean> configParamBeanList = configParamService.getConfigParamBeanListInternal(BtuConfigParamEntity.CHECK_CERT_JOB.CONFIG_GROUP, null);

        // check cert
        List<SdCheckCertBean> checkCertBeanList = certService.checkCert(configParamBeanList);
        String emailBody = certService.formEmailBody(checkCertBeanList);

        try {
            emailService.send(BtuSiteConfigBean.DEFAULT_MAIL_USERNAME, "SdCheckCertJob report", emailBody);
        } catch (MessagingException e) {
            LOG.error(e.getMessage());
        }
    }
}
