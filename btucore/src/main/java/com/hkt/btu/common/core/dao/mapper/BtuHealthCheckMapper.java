package com.hkt.btu.common.core.dao.mapper;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
@Repository
public interface BtuHealthCheckMapper {
    LocalDateTime getDatabaseTime();
}
