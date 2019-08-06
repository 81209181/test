package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.dao.mapper.BtuHealthCheckMapper;
import com.hkt.btu.common.core.service.BtuHealthCheckService;

import javax.annotation.Resource;
import java.time.LocalDateTime;

public class BtuHealthCheckServiceImpl implements BtuHealthCheckService {

    @Resource
    BtuHealthCheckMapper healthCheckMapper;

    @Override
    public LocalDateTime getDatabaseTime() {
        return healthCheckMapper.getDatabaseTime();
    }
}
