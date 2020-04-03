package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SdUserRoleServiceTypeBean;

import java.util.List;

public interface SdUserRoleServiceTypeService {

    List<SdUserRoleServiceTypeBean> getUserRoleServiceType(String roleId);

    void editUserRoleServiceType(String roleId, List<String> serviceTypeList);
}
