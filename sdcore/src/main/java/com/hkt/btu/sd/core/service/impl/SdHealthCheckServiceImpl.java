package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.mapper.SdHealthCheckMapper;
import com.hkt.btu.sd.core.service.SdHealthCheckService;

import javax.annotation.Resource;
import java.time.LocalDateTime;

public class SdHealthCheckServiceImpl implements SdHealthCheckService {

    @Resource
    private SdHealthCheckMapper sdHealthCheckMapper;


    @Override
    public LocalDateTime getDatabaseTime() {
        return sdHealthCheckMapper.getDatabaseTime();
    }
}
