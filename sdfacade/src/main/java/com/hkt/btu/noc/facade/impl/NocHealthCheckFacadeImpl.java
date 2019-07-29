package com.hkt.btu.noc.facade.impl;


import com.hkt.btu.noc.core.service.NocHealthCheckService;
import com.hkt.btu.noc.facade.NocHealthCheckFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.time.LocalDateTime;


public class NocHealthCheckFacadeImpl implements NocHealthCheckFacade {
    private static final Logger LOG = LogManager.getLogger(NocHealthCheckFacadeImpl.class);

    @Resource(name = "healthCheckService")
    NocHealthCheckService nocHealthCheckService;

    @Override
    public String healthCheck() {
        try{
            LocalDateTime databaseTime = nocHealthCheckService.getDatabaseTime();
            LOG.info("Database Time: " + databaseTime);
            return null;
        } catch (Exception e){
            LOG.error(e.getMessage(), e);
            return "Cannot get database time!";
        }
    }
}
