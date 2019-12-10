package com.hkt.btu.sd.core.service;

import org.quartz.JobExecutionException;

public interface SdHistoryService {
    void cleanHistoryData() throws JobExecutionException;
}
