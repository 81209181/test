package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.exception.ClockOutSyncException;
import com.hkt.btu.common.core.service.BtuHealthCheckService;
import com.hkt.btu.sd.core.dao.mapper.SdHistoryMapper;
import com.hkt.btu.sd.core.service.SdHistoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

public class SdHistoryServiceImpl implements SdHistoryService {
    private static final Logger LOG = LogManager.getLogger(SdHistoryServiceImpl.class);

    @Resource(name = "healthCheckService")
    BtuHealthCheckService healthCheckService;

    @Resource
    SdHistoryMapper historyMapper;

    @Override
    public void cleanHistoryData() throws ClockOutSyncException {
        // Check Database date and JVM date in sync
        healthCheckService.checkTimeSync();

        // decide delete cutoff date
        LocalDateTime cutoffDate = LocalDateTime.of(LocalDate.now().minusMonths(18).with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN);
        LOG.info("Cleaning config param history before {}...", cutoffDate);

        // delete
        int deleteCount = historyMapper.cleanConfigParamHistory(cutoffDate);
        LOG.info("Deleted {} row(s) of config param history.", deleteCount);

        LOG.info("Cleaning cron job history before {}...", cutoffDate);
        deleteCount = historyMapper.cleanCronJobHistory(cutoffDate);
        LOG.info("Deleted {} row(s) of cron job history.", deleteCount);

        LOG.info("Cleaning path ctrl history before {}...", cutoffDate);
        deleteCount = historyMapper.cleanPathCtrlHistory(cutoffDate);
        LOG.info("Deleted {} row(s) of path ctrl history.", deleteCount);

        LOG.info("Cleaning user password history before {}...", cutoffDate);
        deleteCount = historyMapper.cleanUserPwdHistory(cutoffDate);
        LOG.info("Deleted {} row(s) of user password history.", deleteCount);

        LOG.info("Cleaning user role history before {}...", cutoffDate);
        deleteCount = historyMapper.cleanUserRoleHistory(cutoffDate);
        LOG.info("Deleted {} row(s) of user role history.", deleteCount);

        LOG.info("Cleaning user role path ctrl history before {}...", cutoffDate);
        deleteCount = historyMapper.cleanUserRolePathCtrlHistory(cutoffDate);
        LOG.info("Deleted {} row(s) of user role path ctrl history.", deleteCount);

        LOG.info("Cleaning user user role history before {}...", cutoffDate);
        deleteCount = historyMapper.cleanUserUserRoleHistory(cutoffDate);
        LOG.info("Deleted {} row(s) of user user role history.", deleteCount);
    }
}
