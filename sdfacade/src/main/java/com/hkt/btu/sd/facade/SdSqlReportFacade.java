package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.data.*;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.List;

public interface SdSqlReportFacade {

    List<SdSqlReportData> getAllReportData();

    List<SdCronJobInstData> getAllReportJobInstance();

    SdSqlReportData getSqlReportDataByReportId(String reportId);

    String resumeReport(String reportName);

    String pauseReport(String reportName);

    String triggerReport(String reportName);

    ResponseReportData createReport(RequestReportData data);

    ResponseReportData deleteReport(String reportId);

    ResponseReportData updateReport(RequestReportData data);

    String activeReport(String reportId);

    String deactiveReport(String reportId);

    String syncReport(String reportId);

    List<SdReportHistoryData> getFileList(String reportId);

    Resource downLoadReport(String reportId, String reportName);
}
