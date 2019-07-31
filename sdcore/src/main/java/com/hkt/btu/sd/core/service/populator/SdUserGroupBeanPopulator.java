package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdUserGroupEntity;
import com.hkt.btu.sd.core.service.bean.SdUserGroupBean;
import org.springframework.stereotype.Component;

public class SdUserGroupBeanPopulator extends AbstractBeanPopulator<SdUserGroupBean> {
    public void populate(SdUserGroupEntity source, SdUserGroupBean target) {
        super.populate(source, target);

        target.setGroupId(source.getGroupId());
        target.setGroupName(source.getGroupName());
        target.setGroupDesc(source.getGroupDesc());
        target.setParentGroup(source.getParentGroup());
    }
}
