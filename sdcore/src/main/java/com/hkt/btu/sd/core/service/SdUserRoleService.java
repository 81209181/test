package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SdUserRoleBean;

import java.util.List;

public interface SdUserRoleService {

    List<SdUserRoleBean> getAllUserRole();
    List<SdUserRoleBean> getUserRoleByUserId(String userId);
    List<SdUserRoleBean> getEligibleUserRoleGrantList();
    boolean isEligibleToGrantUserRole(List<String> roleIdList);
    void updateUserRole(String userId, List<String> roleIdList);
}
