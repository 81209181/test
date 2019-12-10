package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.mapper.SdHistoryMapper;
import com.hkt.btu.sd.core.service.SdHealthCheckService;
import com.hkt.btu.sd.core.service.SdHistoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionException;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class SdHistoryServiceImpl implements SdHistoryService {
    private static final Logger LOG = LogManager.getLogger(SdHistoryServiceImpl.class);

    @Resource(name = "healthCheckService")
    SdHealthCheckService sdHealthCheckService;

    @Resource
    SdHistoryMapper historyMapper;

    @Override
    public void cleanHistoryData() throws JobExecutionException {
        // check db date and jvm date different
        sdHealthCheckService.checkTimeSync();

        String beforeDate = LocalDateTime.now().minusMonths(18).with(TemporalAdjusters.firstDayOfMonth()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LOG.info("Cleaning config param history before {}", beforeDate);
        LOG.info("Deleted {} row(s) of config param history.", historyMapper.cleanConfigParamHistory());

        LOG.info("Cleaning cron job history before {}", beforeDate);
        LOG.info("Deleted {} row(s) of cron job history.", historyMapper.cleanCronJobHistory());

        LOG.info("Cleaning path ctrl history before {}", beforeDate);
        LOG.info("Deleted {} row(s) of path ctrl history.", historyMapper.cleanPathCtrlHistory());

        LOG.info("Cleaning user password history before {}", beforeDate);
        LOG.info("Deleted {} row(s) of user password history.", historyMapper.cleanUserPwdHistory());

        LOG.info("Cleaning user role history before {}", beforeDate);
        LOG.info("Deleted {} row(s) of user role history.", historyMapper.cleanUserRoleHistory());

        LOG.info("Cleaning user role path ctrl history before {}", beforeDate);
        LOG.info("Deleted {} row(s) of user role path ctrl history.", historyMapper.cleanUserRolePathCtrlHistory());

        LOG.info("Cleaning user user role history before {}", beforeDate);
        LOG.info("Deleted {} row(s) of user user role history.", historyMapper.cleanUserUserRoleHistory());
    }
}
