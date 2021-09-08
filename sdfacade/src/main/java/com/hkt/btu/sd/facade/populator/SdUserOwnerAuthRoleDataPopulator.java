package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdUserOwnerAuthRoleBean;
import com.hkt.btu.sd.facade.data.SdUserOwnerAuthRoleData;
import org.springframework.beans.BeanUtils;

public class SdUserOwnerAuthRoleDataPopulator extends AbstractDataPopulator<SdUserOwnerAuthRoleData> {
    public void pupulate(SdUserOwnerAuthRoleBean source, SdUserOwnerAuthRoleData target) {
        BeanUtils.copyProperties(source, target);
    }
}
