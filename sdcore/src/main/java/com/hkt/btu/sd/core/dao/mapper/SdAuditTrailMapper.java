package com.hkt.btu.sd.core.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface SdAuditTrailMapper {

    void insertAuditTrail(@Param("userId") String userId, @Param("action") String action, @Param("detail") String detail);

}
