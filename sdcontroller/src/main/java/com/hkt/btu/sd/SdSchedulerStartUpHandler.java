package com.hkt.btu.sd;

import com.hkt.btu.sd.core.service.SdSchedulerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SdSchedulerStartUpHandler implements ApplicationRunner {
    private static final Logger LOG = LogManager.getLogger(SdSchedulerStartUpHandler.class);


    @Resource(name = "schedulerService")
    private SdSchedulerService sdSchedulerService;


    @SuppressWarnings("RedundantThrows")
    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOG.info("Scheduling jobs at startup.");
        try {
            sdSchedulerService.rescheduleAllCronJobs();
            LOG.info("Complete scheduling jobs at startup.");
        } catch (Exception e) {
            LOG.error("Critical error occurred scheduling jobs at startup.");
            LOG.error(e.getMessage(), e);
        }
    }
}
