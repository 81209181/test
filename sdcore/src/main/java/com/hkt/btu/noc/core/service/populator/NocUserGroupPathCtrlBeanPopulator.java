package com.hkt.btu.noc.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.noc.core.dao.entity.NocUserGroupPathCtrlEntity;
import com.hkt.btu.noc.core.service.bean.NocUserGroupPathCtrlBean;

public class NocUserGroupPathCtrlBeanPopulator extends AbstractBeanPopulator<NocUserGroupPathCtrlBean> {

    public void populate(NocUserGroupPathCtrlEntity source, NocUserGroupPathCtrlBean target) {
        super.populate(source, target);

        // BtuUserGroupPathCtrlBean
        target.setPathCtrlId(source.getPathCtrlId());
        target.setAntPath(source.getAntPath());
        target.setGroupId(source.getGroupId());

        // NocUserGroupPathCtrlBean
        target.setPathCtrlId(source.getPathCtrlId());
    }
}
