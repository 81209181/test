package com.hkt.btu.common.core.job;

import com.hkt.btu.common.core.service.BtuEmailService;
import com.hkt.btu.common.core.service.BtuReportService;
import com.hkt.btu.common.core.service.bean.BtuEmailBean;
import com.hkt.btu.common.core.service.bean.BtuReportMetaDataBean;
import com.hkt.btu.common.core.service.bean.BtuReportProfileBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.File;
import java.util.HashMap;


public class BtuSqlReportJob extends BtuSampleJob {

    private static final Logger LOG = LogManager.getLogger(BtuSqlReportJob.class);

    @Resource(name = "reportService")
    BtuReportService reportService;

    @Resource(name = "emailService")
    BtuEmailService emailService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobKey jobKey = jobDetail.getKey();

        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        BtuReportMetaDataBean metaDataBean = (BtuReportMetaDataBean) jobDataMap.get(BtuReportProfileBean.REPORT_JOBDATAMAP_KEY);
        if (metaDataBean == null) {
            String errorMsg = String.format("Cannot get report job meta data. (jobKey=%s)", jobKey);
            LOG.error(errorMsg);
            throw new JobExecutionException(errorMsg);
        }
        LOG.info("Resolved report meta data. (jobKey={})", jobKey);

        // generate report
        File reportFile = reportService.getCsvFile(metaDataBean);
        if(reportFile==null){
            String errorMsg = String.format("Cannot generate report file. (jobKey=%s)", jobKey);
            LOG.error(errorMsg);
            throw new JobExecutionException(errorMsg);
        }
        LOG.info("Generated report file. (reportFile={})", reportFile.getPath());

        // send report file (if applicable)
        String recipient = metaDataBean.getEmailTo();
        if (StringUtils.isNotEmpty(recipient)) {
            try {
                emailService.send(BtuEmailBean.DEFAULT_EMAIL.TEMPLATE_ID, recipient, reportFile, new HashMap<>());
            } catch (MessagingException e) {
                LOG.error(e.getMessage(), e);

                String errorMsg = String.format("Cannot email report file. (jobKey=%s)", jobKey);
                LOG.error(errorMsg);
                throw new JobExecutionException(errorMsg);
            }

            LOG.info("Emailed report file. (recipient={})", recipient);
        }
        
    }
}

