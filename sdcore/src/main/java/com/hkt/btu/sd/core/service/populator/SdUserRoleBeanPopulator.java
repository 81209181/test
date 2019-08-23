package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdUserRoleEntity;
import com.hkt.btu.sd.core.service.bean.SdUserRoleBean;
import org.springframework.beans.BeanUtils;

public class SdUserRoleBeanPopulator extends AbstractBeanPopulator<SdUserRoleBean> {

    public void populate(SdUserRoleEntity source, SdUserRoleBean target) {
        super.populate(source, target);
        BeanUtils.copyProperties(source, target);
    }
}
