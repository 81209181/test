package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdSqlReportEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SdSqlReportMapper {

    SdSqlReportEntity getSqlReportDataByReportId(@Param("reportId") String reportId);

    List<Map<String, Object>> queryBySQL(String value);

    List<SdSqlReportEntity> getSqlReportData(@Param("status") String status);

    void deleteSqlReportDataByReportId(@Param("reportId") String reportId);

    void createSqlReport(@Param("reportName") String reportName, @Param("cronExp") String cronExp,
                         @Param("status") String status, @Param("sql") String sql, @Param("exportTo") String exportTo,
                         @Param("emailTo") String emailTo, @Param("createBy") String createBy, @Param("modifyBy") String modifyBy,
                         @Param("remarks") String remarks);

    void updateSqlReport(@Param("reportId") String reportId, @Param("reportName") String reportName, @Param("sql") String sql,
                         @Param("cronExp") String cronExp, @Param("exportTo") String exportTo, @Param("emailTo") String emailTo,
                         @Param("modifyBy") String modifyBy, @Param("status") String status, @Param("remarks") String remarks);
}