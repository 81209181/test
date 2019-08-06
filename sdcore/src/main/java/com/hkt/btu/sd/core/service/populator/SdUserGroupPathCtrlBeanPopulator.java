package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdUserGroupPathCtrlEntity;
import com.hkt.btu.sd.core.service.bean.SdUserGroupPathCtrlBean;

public class SdUserGroupPathCtrlBeanPopulator extends AbstractBeanPopulator<SdUserGroupPathCtrlBean> {

    public void populate(SdUserGroupPathCtrlEntity source, SdUserGroupPathCtrlBean target) {
        super.populate(source, target);

        // BtuUserGroupPathCtrlBean
        target.setPathCtrlId(source.getPathCtrlId());
        target.setAntPath(source.getAntPath());
        target.setGroupId(source.getGroupId());

        // SdUserGroupPathCtrlBean
        target.setPathCtrlId(source.getPathCtrlId());
    }
}
