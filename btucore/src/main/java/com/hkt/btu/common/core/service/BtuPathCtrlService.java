package com.hkt.btu.common.core.service;

import com.hkt.btu.common.core.service.bean.BtuUserRolePathCtrlBean;

import java.util.List;

public interface BtuPathCtrlService {
    List <BtuUserRolePathCtrlBean> getActiveCtrlBeanList();
}
