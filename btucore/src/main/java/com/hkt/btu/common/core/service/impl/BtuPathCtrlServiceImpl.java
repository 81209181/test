package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.service.BtuPathCtrlService;
import com.hkt.btu.common.core.service.bean.BtuUserRolePathCtrlBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

public class BtuPathCtrlServiceImpl implements BtuPathCtrlService {
    private static final Logger LOG = LogManager.getLogger(BtuPathCtrlServiceImpl.class);

    @Override
    public List<BtuUserRolePathCtrlBean> getActiveCtrlBeanList() {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        List<BtuUserRolePathCtrlBean> result = new LinkedList<>();

        BtuUserRolePathCtrlBean userCtrlBean = new BtuUserRolePathCtrlBean();
        userCtrlBean.setRoleId("1");
        userCtrlBean.setPath("/user/**");

        BtuUserRolePathCtrlBean adminCtrlBean = new BtuUserRolePathCtrlBean();
        adminCtrlBean.setRoleId("2");
        adminCtrlBean.setPath("/admin/**");

        return result;
    }

}
