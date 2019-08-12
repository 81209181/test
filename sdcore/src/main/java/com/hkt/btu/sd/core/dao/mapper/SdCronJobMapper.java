package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdCronJobEntity;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SdCronJobMapper {
    // select
    List<SdCronJobEntity> getAll();
    SdCronJobEntity getJobByJobGrpJobName(String jobGroup, String jobName);

    // paged select


    // update
    int updateStatus(String jobGroup, String jobName, String status, String modifyby);

    // insert

}
