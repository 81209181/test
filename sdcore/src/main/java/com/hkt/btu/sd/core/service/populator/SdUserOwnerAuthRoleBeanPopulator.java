package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdUserOwnerAuthRoleEntity;
import com.hkt.btu.sd.core.service.bean.SdUserOwnerAuthRoleBean;
import org.springframework.beans.BeanUtils;

public class SdUserOwnerAuthRoleBeanPopulator extends AbstractBeanPopulator<SdUserOwnerAuthRoleBean> {
    public void populate(SdUserOwnerAuthRoleEntity source, SdUserOwnerAuthRoleBean target) {
        BeanUtils.copyProperties(source, target);
    }
}
