package com.hkt.btu.common.core.dao.mapper;

import com.hkt.btu.common.core.dao.entity.BtuCronJobLogEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface BtuCronJobLogMapper {
    void insertLog(BtuCronJobLogEntity cronJobLogEntity);
}
