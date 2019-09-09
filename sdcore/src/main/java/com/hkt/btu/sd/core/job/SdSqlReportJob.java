package com.hkt.btu.sd.core.job;

import com.hkt.btu.common.core.job.BtuSampleJob;
import com.hkt.btu.common.genrator.BtuCSVGenrator;
import com.hkt.btu.sd.core.service.SdEmailService;
import com.hkt.btu.sd.core.service.SdSqlReportProfileService;
import com.hkt.btu.sd.core.service.bean.SdSqlReportBean;
import com.hkt.btu.sd.core.util.SqlFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Map;

public class SdSqlReportJob extends BtuSampleJob {

    public static final String CSV_SUFFIX = ".csv";

    private static final Logger LOG = LogManager.getLogger(SdSqlReportJob.class);

    @Resource(name = "csvGenrator")
    BtuCSVGenrator csvGenrator;

    @Resource(name = "sqlReportProfileService")
    SdSqlReportProfileService sqlReportProfileService;

    @Resource(name = "emailService")
    SdEmailService emailService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        List<SdSqlReportBean> activeReportList = sqlReportProfileService.getAllReportBean(SdSqlReportBean.ACTIVE_STATUS);

        if (CollectionUtils.isEmpty(activeReportList)) {
            LOG.warn("There is no Report Job");
            return;
        }
        for (SdSqlReportBean bean : activeReportList) {
            File csvFile = getCsvFile(bean);
            if (csvFile != null) {
                // Send Email
                // TODO: Send Email Function
            }
        }

    }

    private File getCsvFile(SdSqlReportBean bean) {
        String filePath = getFilePath(bean.getExportTo(), bean.getReportName());
        String sql = getSql(bean.getSql());
        List<Map<String, Object>> results = sqlReportProfileService.executeSql(sql);
        return csvGenrator.generateCSV(filePath, null, results);
    }

    private String getSql(String sql) {
        if (StringUtils.isNotBlank(sql)) {
            sql = SqlFilter.filter(sql);
        }
        return sql;
    }

    private String getFilePath(String exportTo, String reportName) {
        String filePath = "";
        if (reportName.contains(CSV_SUFFIX)) {
            filePath = exportTo + File.separator + reportName;
        } else {
            filePath = exportTo + File.separator + reportName + CSV_SUFFIX;
        }
        return filePath;
    }
}
