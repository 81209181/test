package com.hkt.btu.sd.facade.job;

import com.hkt.btu.sd.core.service.SdEmailService;
import com.hkt.btu.sd.core.service.SdServiceTypeService;
import com.hkt.btu.sd.facade.WfmApiFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import javax.mail.MessagingException;

@DisallowConcurrentExecution
public class OfferNameServiceTypeUpdateJob extends QuartzJobBean {
    private static final Logger LOG = LogManager.getLogger(OfferNameServiceTypeUpdateJob.class);

    @Resource(name = "wfmApiFacade")
    WfmApiFacade wfmApiFacade;

    @Resource(name = "emailService")
    SdEmailService emailService;
    @Resource(name = "serviceTypeService")
    SdServiceTypeService serviceTypeService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            serviceTypeService.updateServiceTypeOfferMappingByJob(wfmApiFacade.getServiceTypeOfferMapping());
        } catch (Exception e) {
            try {
                emailService.sendErrorStackTrace("", "", e);
            } catch (MessagingException ex) {
                LOG.error(ex.getMessage(),ex);
            }
        }
    }
}
