package com.hkt.btu.common.core.service;

import com.hkt.btu.common.core.exception.ClockOutSyncException;

import java.time.LocalDateTime;

public interface BtuHealthCheckService {

    LocalDateTime getDatabaseTime();

    void checkTimeSync() throws ClockOutSyncException;
}
