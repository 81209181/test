package com.hkt.btu.sd.core.service;


import org.quartz.JobExecutionException;

import java.time.LocalDateTime;

public interface SdHealthCheckService {
    LocalDateTime getDatabaseTime();

    void checkTimeSync() throws JobExecutionException;
}
