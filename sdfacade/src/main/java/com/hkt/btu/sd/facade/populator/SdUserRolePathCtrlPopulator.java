package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdUserRolePathCtrlBean;
import com.hkt.btu.sd.facade.data.SdUserPathCtrlData;

public class SdUserRolePathCtrlPopulator extends AbstractDataPopulator<SdUserPathCtrlData> {

    public void populate(SdUserRolePathCtrlBean source, SdUserPathCtrlData target) {
        target.setPathCtrlId(source.getPathCtrlId());
        target.setPath(source.getPath());
        target.setDescription(source.getDescription());
        target.setStatus(source.getStatus());
        target.setCreateby(source.getCreateby());
        target.setCreatedate(source.getCreatedate());
        target.setModifyby(source.getModifyby());
        target.setModifydate(source.getModifydate());
    }
}
