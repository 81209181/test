package com.hkt.btu.common.core.service;

import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;

import java.util.List;

public interface BtuSqlReportProfileService {

    List<BtuCronJobProfileBean> getAllReportData(String status);

    BtuCronJobProfileBean getProfileBeanByGrpAndName(String keyGroup, String reportId);
}
