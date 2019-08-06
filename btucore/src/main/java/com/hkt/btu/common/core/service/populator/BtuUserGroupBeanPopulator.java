package com.hkt.btu.common.core.service.populator;

import com.hkt.btu.common.core.dao.entity.BtuUserGroupEntity;
import com.hkt.btu.common.core.service.bean.BtuUserGroupBean;

public class BtuUserGroupBeanPopulator extends AbstractBeanPopulator<BtuUserGroupBean> {
    public void populate(BtuUserGroupEntity source, BtuUserGroupBean target) {
        super.populate(source, target);

        target.setGroupId(source.getGroupId());
        target.setGroupName(source.getGroupName());
        target.setGroupDesc(source.getGroupDesc());
        target.setParentGroup(source.getParentGroup());
    }
}
