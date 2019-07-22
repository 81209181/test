package com.hkt.btu.common.core.service;

import com.hkt.btu.common.core.service.bean.BtuUserGroupPathCtrlBean;

import java.util.List;

public interface BtuPathCtrlService {
    List <BtuUserGroupPathCtrlBean> getActiveCtrlBeanList();
}
