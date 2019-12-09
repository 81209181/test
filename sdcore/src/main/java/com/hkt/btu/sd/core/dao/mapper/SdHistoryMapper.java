package com.hkt.btu.sd.core.dao.mapper;

import org.springframework.stereotype.Repository;


@Repository
public interface SdHistoryMapper {

    int cleanConfigParamHistory();

    int cleanCronJobHistory();

    int cleanPathCtrlHistory();

    int cleanUserPwdHistory();

    int cleanUserRoleHistory();

    int cleanUserRolePathCtrlHistory();

    int cleanUserUserRoleHistory();
}
