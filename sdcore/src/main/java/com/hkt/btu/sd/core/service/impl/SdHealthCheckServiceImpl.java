package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.exception.ClockOutSyncException;
import com.hkt.btu.sd.core.dao.mapper.SdHealthCheckMapper;
import com.hkt.btu.sd.core.service.SdHealthCheckService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public void checkTimeSync() throws ClockOutSyncException {
        LocalDateTime dbDateTime = sdHealthCheckMapper.getDatabaseTime();
        LocalDateTime jvmDateTime = LocalDateTime.now();
        LOG.info("Database date time: {}", dbDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        LOG.info("JVM date time: {}", jvmDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        long secondDiff = jvmDateTime.until(dbDateTime, ChronoUnit.SECONDS);
        if (Math.abs(secondDiff) > 180) {
            String errorMsg = String.format("Database and JVM not in sync. (db: %s, jvm: %s)", dbDateTime, jvmDateTime);
            throw new ClockOutSyncException(errorMsg);
        }
    }
}
