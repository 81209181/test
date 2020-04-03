package com.hkt.btu.sd.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdUserRoleServiceTypeBean;
import com.hkt.btu.sd.facade.data.SdUserRoleServiceTypeData;

public class SdUserRoleServiceTypeDataPopulator extends AbstractDataPopulator<SdUserRoleServiceTypeData> {

    public void populate(SdUserRoleServiceTypeBean source, SdUserRoleServiceTypeData target) {
        target.setServiceTypeCode(source.getServiceTypeCode());
        target.setRoleId(source.getRoleId());
    }
}
