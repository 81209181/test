package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdUserGroupBean;
import com.hkt.btu.sd.facade.data.SdUserGroupData;

public class SdUserGroupDataPopulator extends AbstractDataPopulator<SdUserGroupData> {

    public void populate(SdUserGroupBean source, SdUserGroupData target) {
        target.setGroupId( source.getGroupId() );
        target.setGroupName( source.getGroupName() );
        target.setGroupDesc( source.getGroupDesc() );
        target.setParentGroup( source.getParentGroup() );
    }
}
