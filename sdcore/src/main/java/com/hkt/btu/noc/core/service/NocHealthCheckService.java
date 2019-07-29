package com.hkt.btu.noc.core.service;


import java.time.LocalDateTime;

public interface NocHealthCheckService {
    LocalDateTime getDatabaseTime();
}
