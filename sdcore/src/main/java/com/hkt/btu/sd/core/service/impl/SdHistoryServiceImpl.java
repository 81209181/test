package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.mapper.SdHistoryMapper;
import com.hkt.btu.sd.core.service.SdHistoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;

public class SdHistoryServiceImpl implements SdHistoryService {
    private static final Logger LOG = LogManager.getLogger(SdHistoryServiceImpl.class);

    @Resource
    SdHistoryMapper historyMapper;

    @Override
    public void cleanHistoryData() {
        LOG.info("Clean config param history data count: {}", historyMapper.cleanConfigParamHistory());
        LOG.info("Clean cron job history data count: {}", historyMapper.cleanCronJobHistory());
        LOG.info("Clean path ctrl history data count: {}", historyMapper.cleanPathCtrlHistory());
        LOG.info("Clean user password history data count: {}", historyMapper.cleanUserPwdHistory());
        LOG.info("Clean user role history data count: {}", historyMapper.cleanUserRoleHistory());
        LOG.info("Clean user role path ctrl history data count: {}", historyMapper.cleanUserRolePathCtrlHistory());
        LOG.info("Clean user user role history data count: {}", historyMapper.cleanUserUserRoleHistory());
    }
}
