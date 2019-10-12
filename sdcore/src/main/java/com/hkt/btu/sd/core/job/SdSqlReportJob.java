package com.hkt.btu.sd.core.job;

import com.hkt.btu.common.core.job.BtuSampleJob;
import com.hkt.btu.common.core.service.bean.BtuEmailBean;
import com.hkt.btu.common.core.service.bean.BtuReportMetaDataBean;
import com.hkt.btu.common.core.service.bean.BtuSqlReportBean;
import com.hkt.btu.common.genrator.BtuCSVGenrator;
import com.hkt.btu.sd.core.service.SdCsvGenratorService;
import com.hkt.btu.sd.core.service.SdEmailService;
import com.hkt.btu.sd.core.service.SdSqlReportProfileService;
import com.hkt.btu.sd.core.service.bean.SdEmailBean;
import com.hkt.btu.sd.core.service.bean.SdSqlReportBean;
import com.hkt.btu.sd.core.util.SqlFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SdSqlReportJob extends BtuSampleJob {

    private static final Logger LOG = LogManager.getLogger(SdSqlReportJob.class);

    @Resource(name = "csvGenratorService")
    SdCsvGenratorService csvGenratorService;

    @Resource(name = "emailService")
    SdEmailService emailService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        BtuReportMetaDataBean metaDataBean = (BtuReportMetaDataBean) jobDataMap.get(SdSqlReportBean.REPORT_JOBDATAMAP_KEY);
        if (metaDataBean == null) {
            LOG.warn("No report meta data.");
            return;
        }
        File csvFile = csvGenratorService.getCsvFile(metaDataBean);
        if (csvFile != null) {
            // Send Email
            if (StringUtils.isNotEmpty(metaDataBean.getEmailTo())) {
                try {
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put(SdEmailBean.EMAIL_BASIC_RECIPIENT_NAME, "Sir/Miss");
                    emailService.send(SdEmailBean.DEFAULT_EMAIL.TEMPLATE_ID, metaDataBean.getEmailTo(), csvFile, dataMap);
                } catch (MessagingException e) {
                    LOG.warn(e);
                }
            }
        }
    }
}
