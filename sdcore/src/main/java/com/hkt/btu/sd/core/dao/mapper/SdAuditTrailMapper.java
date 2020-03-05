package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdAuditTrailStatisticEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface SdAuditTrailMapper {

    void insertAuditTrail(@Param("userId") String userId, @Param("action") String action, @Param("detail") String detail);

    int cleanAuditTrail(LocalDate cutoffDate);

    List<SdAuditTrailStatisticEntity> getLoginCountLast90Days();

    List<SdAuditTrailStatisticEntity> getLoginCountLastTwoWeeks();
}
