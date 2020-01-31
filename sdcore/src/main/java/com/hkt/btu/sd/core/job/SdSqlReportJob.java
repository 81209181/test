package com.hkt.btu.sd.core.job;

import com.hkt.btu.common.core.job.BtuSampleJob;
import com.hkt.btu.common.core.service.bean.BtuReportMetaDataBean;
import com.hkt.btu.sd.core.dao.entity.SdUserEntity;
import com.hkt.btu.sd.core.dao.mapper.SdUserMapper;
import com.hkt.btu.sd.core.service.SdCsvGeneratorService;
import com.hkt.btu.sd.core.service.SdEmailService;
import com.hkt.btu.sd.core.service.bean.SdEmailBean;
import com.hkt.btu.sd.core.service.bean.SdReportProfileBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class SdSqlReportJob extends BtuSampleJob {

    private static final Logger LOG = LogManager.getLogger(SdSqlReportJob.class);

    @Resource(name = "csvGeneratorService")
    SdCsvGeneratorService csvGeneratorService;

    @Resource(name = "emailService")
    SdEmailService emailService;

    @Resource
    SdUserMapper userMapper;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        BtuReportMetaDataBean metaDataBean = (BtuReportMetaDataBean) jobDataMap.get(SdReportProfileBean.REPORT_JOBDATAMAP_KEY);
        if (metaDataBean == null) {
            LOG.warn("No report meta data.");
            return;
        }

        File csvFile = csvGeneratorService.getCsvFile(metaDataBean);
        if (csvFile != null) {
            String email = metaDataBean.getEmailTo();
            SdUserEntity userByEmail = userMapper.getUserByEmail(email);
            if (userByEmail == null) {
                LOG.error("User not found.");
                return;
            }

            // Send Email
            if (StringUtils.isNotEmpty(email)) {
                try {
                    Map<String, Object> dataMap = new HashMap<>(2);
                    dataMap.put(SdEmailBean.EMAIL_BASIC_RECIPIENT_NAME, userByEmail.getName());
                    emailService.send(SdEmailBean.DEFAULT_EMAIL.TEMPLATE_ID, email, csvFile, dataMap);
                } catch (MessagingException e) {
                    LOG.warn(e);
                }
            }
        }
    }
}

