package com.hkt.btu.common.core;

import com.hkt.btu.common.core.service.BtuSchedulerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class BtuSchedulerStartUpHandler implements ApplicationRunner {
    private static final Logger LOG = LogManager.getLogger(BtuSchedulerStartUpHandler.class);


    @Resource(name = "schedulerService")
    private BtuSchedulerService schedulerService;



    @Override
    public void run(ApplicationArguments args) {
        LOG.info("Scheduling jobs at startup.");
        try {
            schedulerService.rescheduleAllCronJobs();
            LOG.info("Complete scheduling jobs at startup.");
        } catch (Exception e) {
            LOG.error("Critical error occurred scheduling jobs at startup.");
            LOG.error(e.getMessage(), e);
        }
    }
}
