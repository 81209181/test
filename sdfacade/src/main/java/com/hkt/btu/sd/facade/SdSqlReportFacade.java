package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.data.RequestReportData;
import com.hkt.btu.sd.facade.data.ResponseReportData;
import com.hkt.btu.sd.facade.data.SdSqlReportData;

import java.util.List;

public interface SdSqlReportFacade {

    List<SdSqlReportData> getAllReportData();
    SdSqlReportData getSqlReportDataByReportId(String reportId);
    String resumeReport(String reportName);
    String pauseReport(String reportName);
    String triggerReport(String reportName);
    ResponseReportData createReport(RequestReportData data);
    ResponseReportData deleteReport(String reportId);
    ResponseReportData updateReport(RequestReportData data);
}