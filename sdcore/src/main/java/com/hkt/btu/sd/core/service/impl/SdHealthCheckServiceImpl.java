package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.impl.BtuHealthCheckServiceImpl;
import com.hkt.btu.sd.core.dao.mapper.SdHealthCheckMapper;
import com.hkt.btu.sd.core.service.SdHealthCheckService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SdHealthCheckServiceImpl extends BtuHealthCheckServiceImpl implements SdHealthCheckService {
    private static final Logger LOG = LogManager.getLogger(SdHealthCheckServiceImpl.class);

    @Resource
    private SdHealthCheckMapper sdHealthCheckMapper;

    @Override
    public LocalDateTime getDatabaseTime() {
        LocalDateTime dbDateTime = sdHealthCheckMapper.getDatabaseTime();
        LOG.info("Database date time: {}", dbDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return dbDateTime;
    }
}
