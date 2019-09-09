package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.service.BtuSqlReportProfileService;
import com.hkt.btu.sd.core.service.bean.SdSqlReportBean;

import java.util.List;
import java.util.Map;

public interface SdSqlReportProfileService extends BtuSqlReportProfileService {

    List<SdSqlReportBean> getAllReportBean(String status);

    List<Map<String, Object>> executeSql(String sql);

}
