package com.hkt.btu.common.core.service;

import com.hkt.btu.common.core.service.bean.BtuSqlReportBean;

import java.util.List;

public interface BtuReportProfileService {

    List<BtuSqlReportBean> getAllReportData(String status);

    BtuSqlReportBean getProfileBeanByGrpAndName(String reportId);

    boolean isRunnable(BtuSqlReportBean bean);
    boolean isRunnable(String reportId);
}
