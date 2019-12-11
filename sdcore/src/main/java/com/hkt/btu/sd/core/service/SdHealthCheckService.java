package com.hkt.btu.sd.core.service;


import com.hkt.btu.sd.core.exception.ClockOutSyncException;

import java.time.LocalDateTime;

public interface SdHealthCheckService {
    LocalDateTime getDatabaseTime();

    void checkTimeSync() throws ClockOutSyncException;
}
