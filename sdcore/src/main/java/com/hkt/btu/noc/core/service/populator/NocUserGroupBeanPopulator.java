package com.hkt.btu.noc.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.noc.core.dao.entity.NocUserGroupEntity;
import com.hkt.btu.noc.core.service.bean.NocUserGroupBean;
import org.springframework.stereotype.Component;

@Component
public class NocUserGroupBeanPopulator extends AbstractBeanPopulator<NocUserGroupBean> {
    public void populate(NocUserGroupEntity source, NocUserGroupBean target) {
        super.populate(source, target);

        target.setGroupId(source.getGroupId());
        target.setGroupName(source.getGroupName());
        target.setGroupDesc(source.getGroupDesc());
        target.setParentGroup(source.getParentGroup());
    }
}
