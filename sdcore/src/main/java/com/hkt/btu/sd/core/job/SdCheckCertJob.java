package com.hkt.btu.sd.core.job;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.service.BtuConfigParamService;
import com.hkt.btu.sd.core.service.SdCertService;
import com.hkt.btu.sd.core.service.SdEmailService;
import com.hkt.btu.sd.core.service.SdSiteConfigService;
import com.hkt.btu.sd.core.service.bean.SdCheckCertBean;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@DisallowConcurrentExecution
public class SdCheckCertJob extends QuartzJobBean {

    @Resource(name = "siteConfigService")
    SdSiteConfigService siteConfigService;
    @Resource(name = "configParamService")
    BtuConfigParamService configParamService;
    @Resource(name = "certService")
    SdCertService certService;
    @Resource(name = "emailService")
    SdEmailService emailService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        final String JOB_ID = BtuConfigParamEntity.CHECK_CERT_JOB.CONFIG_GROUP;

        // find out the path, which need to check cert
        Map<String, Object> configParamBeanMap = configParamService.getConfigParamByConfigGroup(JOB_ID, true);
        List<String> hostList = MapUtils.isEmpty(configParamBeanMap) ? null :
                configParamBeanMap.values().stream()
                        .filter(o -> o instanceof String)
                        .map(o -> (String) o)
                        .collect(Collectors.toList()
                        );

        // check cert
        List<SdCheckCertBean> checkCertBeanList = certService.checkCert(hostList);
        String emailBody = certService.formEmailBody(checkCertBeanList);

        // send feedback email
        try {
            String recipient = configParamService.getString(JOB_ID, BtuConfigParamEntity.CHECK_CERT_JOB.CONFIG_KEY.RECIPIENT);
            recipient = StringUtils.isEmpty(recipient) ? siteConfigService.getSiteConfigBean().getSystemSupportEmail() : recipient;
            String[] recipients = recipient.split(",");
            for (String reci : recipients) {
                emailService.send(reci, null, "SdCheckCertJob report", emailBody);
            }
        } catch (MessagingException e) {
            throw new JobExecutionException(e);
        }
    }
}
