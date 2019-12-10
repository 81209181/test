package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.mapper.SdHealthCheckMapper;
import com.hkt.btu.sd.core.service.SdHealthCheckService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionException;

import javax.annotation.Resource;
import java.time.LocalDateTime;

public class SdHealthCheckServiceImpl implements SdHealthCheckService {

    private static final Logger LOG = LogManager.getLogger(SdHealthCheckServiceImpl.class);

    @Resource
    private SdHealthCheckMapper sdHealthCheckMapper;


    @Override
    public LocalDateTime getDatabaseTime() {
        return sdHealthCheckMapper.getDatabaseTime();
    }

    @Override
    public void checkTimeSync() throws JobExecutionException {
        LocalDateTime dbDateTime = sdHealthCheckMapper.getDatabaseTime();
        LocalDateTime jvmDateTime = LocalDateTime.now();
        LOG.info("database date time: {}", dbDateTime);
        LOG.info("jvm date time: {}", jvmDateTime);

        java.time.Duration duration = java.time.Duration.between(dbDateTime, jvmDateTime);
        if (duration.toMinutes()  >= 0 && duration.toMinutes() <= 3) {
        } else {
            throw new JobExecutionException("The difference between the database date and the jvm date is more than 3 mins.");
        }
    }
}
