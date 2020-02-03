package com.hkt.btu.sd.core.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface SdCronJobLogMapper {
    // select


    // paged select


    // update


    // insert
    void insertLog(@Param("serverHostname") String serverHostname, @Param("serverIp") String serverIp,
                   @Param("jobGroup") String jobGroup, @Param("jobName") String jobName, @Param("jobClass") String jobClass,
                   @Param("action") String action, @Param("createby") String createby);
}
