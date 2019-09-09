package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.data.SdSqlReportData;

import java.util.List;

public interface SdSqlReportFacade {

    List<SdSqlReportData> getAllReportData();
    String resumeReport(String reportName);
    String pauseReport(String reportName);
    String triggerReport(String reportName);
    String createReport();
    String deleteReport();
    String updateReport();
}
