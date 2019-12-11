package com.hkt.btu.sd.core.dao.mapper;

import org.springframework.stereotype.Repository;


@Repository
public interface SdHistoryMapper {

    int cleanConfigParamHistory(String beforeDate);

    int cleanCronJobHistory(String beforeDate);

    int cleanPathCtrlHistory(String beforeDate);

    int cleanUserPwdHistory(String beforeDate);

    int cleanUserRoleHistory(String beforeDate);

    int cleanUserRolePathCtrlHistory(String beforeDate);

    int cleanUserUserRoleHistory(String beforeDate);
}
