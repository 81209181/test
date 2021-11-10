package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.exception.ClockOutSyncException;
import com.hkt.btu.common.core.service.BtuHealthCheckService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class BtuHealthCheckServiceImpl implements BtuHealthCheckService {
    private static final Logger LOG = LogManager.getLogger(BtuHealthCheckServiceImpl.class);

    @Override
    public LocalDateTime getDatabaseTime() {
        LOG.warn("Cannot get db time in btu service layer.");
        throw new NullPointerException("Cannot get db time in btu service layer.");
    }

    @Override
    public void checkTimeSync() throws ClockOutSyncException {
        // get clocks
        LocalDateTime dbDateTime = getDatabaseTime();
        LocalDateTime jvmDateTime = LocalDateTime.now();
        LOG.info("JVM date time: {}", jvmDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // check clocks difference
        long secondDiff = Math.abs( jvmDateTime.until(dbDateTime, ChronoUnit.SECONDS) );
        if (secondDiff > 180) {
            String errorMsg = String.format("Database and JVM not in sync. (db: %s, jvm: %s)", dbDateTime, jvmDateTime);
            throw new ClockOutSyncException(errorMsg);
        }
    }
}
