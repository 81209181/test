package com.hkt.btu.sd.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeUserRoleBean;
import com.hkt.btu.sd.facade.data.SdServiceTypeUserRoleData;

public class SdServiceTypeUserRoleDataPopulator extends AbstractDataPopulator<SdServiceTypeUserRoleData> {

    public void populate(SdServiceTypeUserRoleBean source, SdServiceTypeUserRoleData target) {
        target.setServiceTypeCode(source.getServiceTypeCode());
        target.setRoleId(source.getRoleId());
    }
}
