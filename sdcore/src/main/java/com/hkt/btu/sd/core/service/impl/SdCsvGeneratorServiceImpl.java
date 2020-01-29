package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.bean.BtuReportMetaDataBean;
import com.hkt.btu.common.core.service.BtuCsvService;
import com.hkt.btu.sd.core.service.SdCsvGeneratorService;
import com.hkt.btu.sd.core.service.SdSqlReportProfileService;
import com.hkt.btu.sd.core.util.SqlFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class SdCsvGeneratorServiceImpl implements SdCsvGeneratorService {
    private static final Logger LOG = LogManager.getLogger(SdCsvGeneratorServiceImpl.class);

    private static final String CSV_SUFFIX = ".csv";
    private static final DateTimeFormatter FILE_TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    @Resource(name = "csvGenerator")
    BtuCsvService csvGenerator;

    @Resource(name = "sqlReportProfileService")
    SdSqlReportProfileService sqlReportProfileService;

    @Override
    public File getCsvFile(BtuReportMetaDataBean metaDataBean) {
        String filePath = getFilePath(metaDataBean.getExportTo(), metaDataBean.getReportName());
        String sql = getSql(metaDataBean.getSql());
        List<Map<String, Object>> results = sqlReportProfileService.executeSql(sql);
        return csvGenerator.generateCSV(filePath, null, results);
    }

    private String getSql(String sql) {
        if (StringUtils.isNotBlank(sql)) {
            sql = SqlFilter.filter(sql);
        }
        return sql;
    }

    private String getFilePath(String dirPath, String reportName) {
        final LocalDateTime NOW = LocalDateTime.now();
        // full path: /opt/report/${reportName}/${reportName}_${yyyymmdd_hh24miss}

        // prepare file dir
        String filePath = dirPath + File.separator + reportName;
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // prepare file path
        String formatLocalDateTime = NOW.format(FILE_TIMESTAMP_FORMATTER);
        String fileName = reportName + "_" +formatLocalDateTime + CSV_SUFFIX;

        return filePath + File.separator + fileName;
    }

}
