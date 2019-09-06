package com.hkt.btu.sd.core.dao.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SdSqlReportMapper {

    List<Map<String, Object>> queryBySQL(String value);
}
