package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdUserRoleServiceTypeEntity;
import com.hkt.btu.sd.core.service.bean.SdUserRoleServiceTypeBean;

public class SdUserRoleServiceTypeBeanPopulator extends AbstractBeanPopulator<SdUserRoleServiceTypeBean> {

    public void populate(SdUserRoleServiceTypeEntity source, SdUserRoleServiceTypeBean target) {
        target.setServiceTypeCode(source.getServiceTypeCode());
        target.setRoleId(source.getRoleId());
    }


}
