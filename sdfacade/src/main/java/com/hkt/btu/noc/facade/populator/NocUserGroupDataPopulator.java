package com.hkt.btu.noc.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.noc.core.service.bean.NocUserGroupBean;
import com.hkt.btu.noc.facade.data.NocUserGroupData;

public class NocUserGroupDataPopulator extends AbstractDataPopulator<NocUserGroupData> {

    public void populate(NocUserGroupBean source, NocUserGroupData target) {
        target.setGroupId( source.getGroupId() );
        target.setGroupName( source.getGroupName() );
        target.setGroupDesc( source.getGroupDesc() );
        target.setParentGroup( source.getParentGroup() );
    }
}
