package com.hkt.btu.sd.core.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


@Repository
public interface SdAuditTrailMapper {

    void insertAuditTrail(@Param("userId") String userId, @Param("action") String action, @Param("detail") String detail);

    int cleanAuditTrail(LocalDate cutoffDate);
}
