package com.hkt.btu.common.core.dao.mapper;

import com.hkt.btu.common.core.dao.entity.BtuCronJobEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BtuCronJobMapper {
    BtuCronJobEntity getJobByJobGrpJobName(String jobGroup, String jobName);

    List<BtuCronJobEntity> getAll();

    int updateStatus(String jobGroup, String jobName, String status, Integer modifyby);
}
