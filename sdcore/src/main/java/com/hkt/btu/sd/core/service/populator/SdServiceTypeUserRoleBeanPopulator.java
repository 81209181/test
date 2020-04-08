package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdServiceTypeUserRoleEntity;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeUserRoleBean;

public class SdServiceTypeUserRoleBeanPopulator extends AbstractBeanPopulator<SdServiceTypeUserRoleBean> {

    public void populate(SdServiceTypeUserRoleEntity source, SdServiceTypeUserRoleBean target) {
        target.setServiceTypeCode(source.getServiceTypeCode());
        target.setRoleId(source.getRoleId());
    }


}
