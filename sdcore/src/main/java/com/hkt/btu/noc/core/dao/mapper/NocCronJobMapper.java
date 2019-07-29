package com.hkt.btu.noc.core.dao.mapper;

import com.hkt.btu.noc.core.dao.entity.NocCronJobEntity;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NocCronJobMapper {
    // select
    List<NocCronJobEntity> getAll();
    NocCronJobEntity getJobByJobGrpJobName(String jobGroup, String jobName);

    // paged select


    // update
    int updateStatus(String jobGroup, String jobName, String status, Integer modifyby);

    // insert

}
