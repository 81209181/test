package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.service.BtuPathCtrlService;
import com.hkt.btu.sd.core.service.bean.SdUserRoleBean;
import com.hkt.btu.sd.core.service.bean.SdUserRolePathCtrlBean;

import java.util.List;

public interface SdPathCtrlService extends BtuPathCtrlService {

    List<SdUserRolePathCtrlBean> getParentRolePathByRoleId(String roleId);

    List<SdUserRoleBean> getAbstractParentRole(String roldId);

    List<SdUserRolePathCtrlBean> getActivePathCtrl();

    String createUserRolePathCtrl(String roleId, List<Integer> pathCtrlIdList);

    String delUserRolePathCtrl(String roleId, int pachCtrlId);
}
