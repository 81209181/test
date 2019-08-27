package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdUserRoleBean;
import com.hkt.btu.sd.facade.data.SdUserRoleData;

public class SdUserRoleDataPopulator extends AbstractDataPopulator<SdUserRoleData> {

    public void populate(SdUserRoleBean source, SdUserRoleData target) {
        target.setRoleId(source.getRoleId());
        target.setRoleDesc(source.getRoleDesc());
        target.setParentRoleId(source.getParentRoleId());
        target.setStatus(source.getStatus());
    }
}
