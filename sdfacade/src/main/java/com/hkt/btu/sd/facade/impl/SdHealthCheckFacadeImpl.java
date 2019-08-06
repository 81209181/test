package com.hkt.btu.sd.facade.impl;


import com.hkt.btu.sd.core.service.SdHealthCheckService;
import com.hkt.btu.sd.facade.SdHealthCheckFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.time.LocalDateTime;


public class SdHealthCheckFacadeImpl implements SdHealthCheckFacade {
    private static final Logger LOG = LogManager.getLogger(SdHealthCheckFacadeImpl.class);

    @Resource(name = "healthCheckService")
    SdHealthCheckService sdHealthCheckService;

    @Override
    public String healthCheck() {
        try{
            LocalDateTime databaseTime = sdHealthCheckService.getDatabaseTime();
            LOG.info("Database Time: " + databaseTime);
            return null;
        } catch (Exception e){
            LOG.error(e.getMessage(), e);
            return "Cannot get database time!";
        }
    }
}
