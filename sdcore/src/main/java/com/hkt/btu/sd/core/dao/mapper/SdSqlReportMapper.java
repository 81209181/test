package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.common.core.dao.entity.BtuReportProfileEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SdSqlReportMapper {

    List<BtuReportProfileEntity> getReportProfile(@Param("reportId") String reportId, @Param("reportName") String reportName);
    List<Map<String, Object>> queryWithSql(@Param("sql") String sql);

    int deleteSqlReportDataByReportId(@Param("reportId") String reportId);

    void createSqlReport(BtuReportProfileEntity entity);

    int updateReportData(@Param("reportId") String reportId, @Param("reportName") String reportName, @Param("sql") String sql,
                         @Param("cronExp") String cronExp, @Param("emailTo") String emailTo,
                         @Param("modifyBy") String modifyBy, @Param("status") String status, @Param("remarks") String remarks);
}
