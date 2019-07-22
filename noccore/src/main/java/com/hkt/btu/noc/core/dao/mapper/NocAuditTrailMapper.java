package com.hkt.btu.noc.core.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface NocAuditTrailMapper {

    void insertAuditTrail(@Param("userId") Integer userId, @Param("action") String action, @Param("detail") String detail);

}
