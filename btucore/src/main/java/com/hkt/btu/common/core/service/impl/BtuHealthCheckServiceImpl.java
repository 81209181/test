package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.exception.ClockOutSyncException;
import com.hkt.btu.common.core.service.BtuHealthCheckService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

public class BtuHealthCheckServiceImpl implements BtuHealthCheckService {
    private static final Logger LOG = LogManager.getLogger(BtuHealthCheckServiceImpl.class);

    @Override
    public LocalDateTime getDatabaseTime() {
        return null;
    }

    @Override
    public void checkTimeSync() throws ClockOutSyncException {

    }
}
