package com.hkt.btu.noc.core.dao.mapper;

import com.hkt.btu.noc.core.dao.entity.NocCronJobLogEntity;
import org.springframework.stereotype.Repository;


@Repository
public interface NocCronJobLogMapper {
    // select


    // paged select


    // update


    // insert
    void insertLog(NocCronJobLogEntity nocCronJobLogEntity);
}
