package com.hkt.btu.sd.facade.impl;


import com.hkt.btu.common.core.service.BtuHealthCheckService;
import com.hkt.btu.sd.facade.SdHealthCheckFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.time.LocalDateTime;

public class SdHealthCheckFacadeImpl implements SdHealthCheckFacade {
    private static final Logger LOG = LogManager.getLogger(SdHealthCheckFacadeImpl.class);

    @Resource(name = "healthCheckService")
    BtuHealthCheckService healthCheckService;

    @Override
    public String healthCheck() {
        try{
            LocalDateTime databaseTime = healthCheckService.getDatabaseTime();
            LOG.info("Database Time: " + databaseTime);
            return null;
        } catch (Exception e){
            LOG.error(e.getMessage(), e);
            return "Cannot get database time!";
        }
    }
}
