package com.hkt.btu.sd.core.dao.mapper;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


@Repository
public interface SdHistoryMapper {

    int cleanConfigParamHistory(LocalDateTime cutoffDate);

    int cleanCronJobHistory(LocalDateTime cutoffDate);

    int cleanPathCtrlHistory(LocalDateTime cutoffDate);

    int cleanUserPwdHistory(LocalDateTime cutoffDate);

    int cleanUserRoleHistory(LocalDateTime cutoffDate);

    int cleanUserRolePathCtrlHistory(LocalDateTime cutoffDate);

    int cleanUserUserRoleHistory(LocalDateTime cutoffDate);
}
