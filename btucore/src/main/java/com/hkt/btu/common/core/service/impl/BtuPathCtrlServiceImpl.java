package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.service.BtuPathCtrlService;
import com.hkt.btu.common.core.service.bean.BtuUserGroupPathCtrlBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

public class BtuPathCtrlServiceImpl implements BtuPathCtrlService {
    private static final Logger LOG = LogManager.getLogger(BtuPathCtrlServiceImpl.class);

    @Override
    public List<BtuUserGroupPathCtrlBean> getActiveCtrlBeanList() {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        List<BtuUserGroupPathCtrlBean> result = new LinkedList<>();

        BtuUserGroupPathCtrlBean userCtrlBean = new BtuUserGroupPathCtrlBean();
        userCtrlBean.setGroupId("USER");
        userCtrlBean.setAntPath("/user/**");

        BtuUserGroupPathCtrlBean adminCtrlBean = new BtuUserGroupPathCtrlBean();
        adminCtrlBean.setGroupId("ADMIN");
        adminCtrlBean.setAntPath("/admin/**");

        return result;
    }

}
