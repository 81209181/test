package com.hkt.btu.common.core.service;

import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import com.hkt.btu.common.core.service.bean.BtuSqlReportBean;

import java.util.List;

public interface BtuSqlReportProfileService {

    List<BtuSqlReportBean> getAllReportData(String status);

    BtuSqlReportBean getProfileBeanByGrpAndName(String reportId);

    boolean isRunnable(BtuSqlReportBean bean);

    boolean isRunnable(String reportId);
}
