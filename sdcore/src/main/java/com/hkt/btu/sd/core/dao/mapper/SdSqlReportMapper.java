package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdSqlReportEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SdSqlReportMapper {

    List<Map<String, Object>> queryBySQL(String value);

    List<SdSqlReportEntity> getSqlReportData(@Param("status") String status);
}
