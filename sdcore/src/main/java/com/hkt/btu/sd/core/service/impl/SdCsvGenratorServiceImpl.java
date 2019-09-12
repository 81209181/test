package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.bean.BtuReportMetaDataBean;
import com.hkt.btu.common.genrator.BtuCSVGenrator;
import com.hkt.btu.sd.core.job.SdSqlReportJob;
import com.hkt.btu.sd.core.service.SdCsvGenratorService;
import com.hkt.btu.sd.core.service.SdSqlReportProfileService;
import com.hkt.btu.sd.core.util.SqlFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Map;

public class SdCsvGenratorServiceImpl implements SdCsvGenratorService {

    public static final String CSV_SUFFIX = ".csv";

    private static final Logger LOG = LogManager.getLogger(SdCsvGenratorServiceImpl.class);

    @Resource(name = "csvGenrator")
    BtuCSVGenrator csvGenrator;

    @Resource(name = "sqlReportProfileService")
    SdSqlReportProfileService sqlReportProfileService;

    @Override
    public File getCsvFile(BtuReportMetaDataBean metaDataBean) {
        String filePath = getFilePath(metaDataBean.getExportTo(), metaDataBean.getReportName());
        String sql = getSql(metaDataBean.getSql());
        List<Map<String, Object>> results = sqlReportProfileService.executeSql(sql);
        return csvGenrator.generateCSV(filePath, null, results, null);
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
