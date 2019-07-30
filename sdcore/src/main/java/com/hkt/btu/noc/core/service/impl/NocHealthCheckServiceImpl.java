package com.hkt.btu.noc.core.service.impl;

import com.hkt.btu.noc.core.dao.mapper.NocHealthCheckMapper;
import com.hkt.btu.noc.core.service.NocHealthCheckService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class NocHealthCheckServiceImpl implements NocHealthCheckService {

    @Resource
    private NocHealthCheckMapper nocHealthCheckMapper;


    @Override
    public LocalDateTime getDatabaseTime() {
        return nocHealthCheckMapper.getDatabaseTime();
    }
}
