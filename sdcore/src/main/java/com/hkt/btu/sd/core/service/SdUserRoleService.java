package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SdUserRoleBean;

import java.util.List;

public interface SdUserRoleService {

    void getTeamHeadList();
    List<SdUserRoleBean> getAllUserRole();
    SdUserRoleBean getUserRoleByRoleId(String roleId);
    List<SdUserRoleBean> getUserRoleByUserId(String userId);
    List<SdUserRoleBean> getEligibleUserRoleGrantList();
    boolean isEligibleToGrantUserRole(List<String> roleIdList);
    void updateUserRoleByUserId(String userId, List<String> roleIdList);
    boolean updateUserRole(String roleId, String roleDesc, String status);
}
