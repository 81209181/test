package com.hkt.btu.noc;

import com.hkt.btu.noc.core.service.NocSchedulerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class NocSchedulerStartUpHandler implements ApplicationRunner {
    private static final Logger LOG = LogManager.getLogger(NocSchedulerStartUpHandler.class);


    @Autowired
    private NocSchedulerService nocSchedulerService;


    @SuppressWarnings("RedundantThrows")
    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOG.info("Scheduling jobs at startup.");
        try {
            nocSchedulerService.rescheduleAllCronJobs();
            LOG.info("Complete scheduling jobs at startup.");
        } catch (Exception e) {
            LOG.error("Critical error occurred scheduling jobs at startup.");
            LOG.error(e.getMessage(), e);
        }
    }
}
