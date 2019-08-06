package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdCronJobLogEntity;
import org.springframework.stereotype.Repository;


@Repository
public interface SdCronJobLogMapper {
    // select


    // paged select


    // update


    // insert
    void insertLog(SdCronJobLogEntity sdCronJobLogEntity);
}
