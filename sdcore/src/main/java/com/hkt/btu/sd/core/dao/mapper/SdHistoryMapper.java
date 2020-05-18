package com.hkt.btu.sd.core.dao.mapper;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;


@Repository
public interface SdHistoryMapper {

    int cleanConfigParamHistory(LocalDate cutoffDate);
    int cleanCronJobHistory(LocalDate cutoffDate);
    int cleanPathCtrlHistory(LocalDate cutoffDate);
    int cleanUserRoleHistory(LocalDate cutoffDate);
    int cleanUserRolePathCtrlHistory(LocalDate cutoffDate);
    int cleanUserUserRoleHistory(LocalDate cutoffDate);


}
