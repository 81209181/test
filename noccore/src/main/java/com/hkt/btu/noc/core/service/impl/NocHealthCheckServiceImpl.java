package com.hkt.btu.noc.core.service.impl;

import com.hkt.btu.noc.core.dao.mapper.NocHealthCheckMapper;
import com.hkt.btu.noc.core.service.NocHealthCheckService;

import javax.annotation.Resource;
import java.time.LocalDateTime;

public class NocHealthCheckServiceImpl implements NocHealthCheckService {

    @Resource
    private NocHealthCheckMapper nocHealthCheckMapper;


    @Override
    public LocalDateTime getDatabaseTime() {
        return nocHealthCheckMapper.getDatabaseTime();
    }
}
