package com.hkt.btu.common.core.service;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.bean.BtuReportMetaDataBean;
import com.hkt.btu.common.core.service.bean.BtuReportProfileBean;
import com.hkt.btu.common.core.service.constant.BtuJobStatusEnum;
import org.quartz.SchedulerException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface BtuReportService {

    List<Map<String, Object>> executeSql(String sql);

    // report profiles
    List<BtuReportProfileBean> getAllReportProfiles();
    BtuReportProfileBean getReportProfileByReportId(String reportId);
    BtuReportProfileBean getSqlReportProfileByReportName(String reportName);

    boolean isRunnable(BtuReportProfileBean bean);
    boolean isRunnable(String reportId);

    String createReportProfile(String reportName, String cronExpression, BtuJobStatusEnum status,
                               String sql, String emailTo, String remarks);
    String deleteReportProfile(String reportId);
    String updateReportProfile(String reportId, String reportName, String cronExpression, BtuJobStatusEnum status,
                               String sql, String emailTo, String remarks);

    String activateReportProfile(String reportId);
    String deactivateReportProfile(String reportId);

    File getCsvFile(BtuReportMetaDataBean metaDataBean);

    String getReportDirPath(BtuReportProfileBean reportProfileBean);
    Stream<Path> getReportFileList(String reportId) throws IOException;

    // report scheduling instance
    void scheduleReportJob(BtuReportProfileBean reportProfileBean)
            throws SchedulerException, InvalidInputException, ClassNotFoundException;

}
