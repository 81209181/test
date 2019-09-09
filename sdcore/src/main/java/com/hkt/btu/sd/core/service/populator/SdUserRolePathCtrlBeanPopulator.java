package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdUserGroupPathCtrlEntity;
import com.hkt.btu.sd.core.dao.entity.SdUserRolePathCtrlEntity;
import com.hkt.btu.sd.core.service.bean.SdUserRolePathCtrlBean;

public class SdUserRolePathCtrlBeanPopulator extends AbstractBeanPopulator<SdUserRolePathCtrlBean> {

    public void populate(SdUserRolePathCtrlEntity source, SdUserRolePathCtrlBean target) {
        super.populate(source, target);

        // BtuUserRolePathCtrlBean
        target.setPathCtrlId(source.getPathCtrlId());
        target.setPath(source.getPath());
        target.setRoleId(source.getRoleId());
        target.setStatus(source.getStatus());
        // SdUserRolePathCtrlBean
        target.setDescription(source.getDescription());
        // BaseBean
        target.setCreatedate(source.getCreatedate());
        target.setCreateby(source.getCreateby());
        target.setModifydate(source.getModifydate());
        target.setModifyby(source.getModifyby());
    }
}
