package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.mapper.SdHealthCheckMapper;
import com.hkt.btu.sd.core.service.SdHealthCheckService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionException;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
        LOG.info("db date time: {}", dbDateTime);
        LOG.info("jvm date time: {}", jvmDateTime);

        long secondDiff = jvmDateTime.until(dbDateTime, ChronoUnit.SECONDS);
        if (Math.abs(secondDiff) > 180) {
            String errorMsg = String.format("Database and JVM not in sync. (db: %s, jvm: %s)", dbDateTime, jvmDateTime);
            throw new JobExecutionException(errorMsg);
        }
    }
}
