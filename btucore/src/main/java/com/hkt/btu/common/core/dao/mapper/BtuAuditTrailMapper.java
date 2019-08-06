package com.hkt.btu.common.core.dao.mapper;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BtuAuditTrailMapper {
    void insertAuditTrail(@Param("userId") Integer userId, @Param("action") String action, @Param("detail") String detail);
}
