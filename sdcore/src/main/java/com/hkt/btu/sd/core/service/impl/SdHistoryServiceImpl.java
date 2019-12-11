package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.mapper.SdHistoryMapper;
import com.hkt.btu.sd.core.exception.ClockOutSyncException;
import com.hkt.btu.sd.core.service.SdHealthCheckService;
import com.hkt.btu.sd.core.service.SdHistoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class SdHistoryServiceImpl implements SdHistoryService {
    private static final Logger LOG = LogManager.getLogger(SdHistoryServiceImpl.class);

    @Resource(name = "healthCheckService")
    SdHealthCheckService sdHealthCheckService;

    @Resource
    SdHistoryMapper historyMapper;

    @Override
    public void cleanHistoryData() throws ClockOutSyncException {
        // Check Database date and JVM date in sync
        sdHealthCheckService.checkTimeSync();

        String beforeDate = LocalDateTime.of(LocalDate.now().minusMonths(18).with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LOG.info("Cleaning config param history before {}", beforeDate);
        LOG.info("Deleted {} row(s) of config param history.", historyMapper.cleanConfigParamHistory(beforeDate));

        LOG.info("Cleaning cron job history before {}", beforeDate);
        LOG.info("Deleted {} row(s) of cron job history.", historyMapper.cleanCronJobHistory(beforeDate));

        LOG.info("Cleaning path ctrl history before {}", beforeDate);
        LOG.info("Deleted {} row(s) of path ctrl history.", historyMapper.cleanPathCtrlHistory(beforeDate));

        LOG.info("Cleaning user password history before {}", beforeDate);
        LOG.info("Deleted {} row(s) of user password history.", historyMapper.cleanUserPwdHistory(beforeDate));

        LOG.info("Cleaning user role history before {}", beforeDate);
        LOG.info("Deleted {} row(s) of user role history.", historyMapper.cleanUserRoleHistory(beforeDate));

        LOG.info("Cleaning user role path ctrl history before {}", beforeDate);
        LOG.info("Deleted {} row(s) of user role path ctrl history.", historyMapper.cleanUserRolePathCtrlHistory(beforeDate));

        LOG.info("Cleaning user user role history before {}", beforeDate);
        LOG.info("Deleted {} row(s) of user user role history.", historyMapper.cleanUserUserRoleHistory(beforeDate));
    }
}
