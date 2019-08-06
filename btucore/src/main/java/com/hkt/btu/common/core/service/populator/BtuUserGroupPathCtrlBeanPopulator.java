package com.hkt.btu.common.core.service.populator;

import com.hkt.btu.common.core.dao.entity.BtuUserGroupPathCtrlEntity;
import com.hkt.btu.common.core.service.bean.BtuUserGroupPathCtrlBean;

public class BtuUserGroupPathCtrlBeanPopulator extends AbstractBeanPopulator<BtuUserGroupPathCtrlBean> {

    public void populate(BtuUserGroupPathCtrlEntity source, BtuUserGroupPathCtrlBean target) {
        super.populate(source, target);

        // BtuUserGroupPathCtrlBean
        target.setPathCtrlId(source.getPathCtrlId());
        target.setAntPath(source.getAntPath());
        target.setGroupId(source.getGroupId());

        // SdUserGroupPathCtrlBean
        target.setPathCtrlId(source.getPathCtrlId());
    }
}
