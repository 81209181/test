package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.BtuReportProfileService;
import com.hkt.btu.sd.core.service.bean.SdSqlReportBean;

import java.util.List;
import java.util.Map;

public interface SdReportProfileService extends BtuReportProfileService {

    List<SdSqlReportBean> getAllReportBean(String status);

    List<Map<String, Object>> executeSql(String sql);

    SdSqlReportBean getSqlReportDataByReportId(String reportId);

    String createReport(String reportName, String cronExpression, String status,
                        String sql, String emailTo, String remarks);

    String deleteReport(String reportId);

    String updateReport(String reportId,String reportName,String cronExpression, String status,
                        String sql, String emailTo, String remarks);

    void activateReportProfile(String reportId) throws InvalidInputException;

    void deactivateReportProfile(String reportId) throws InvalidInputException;
}
