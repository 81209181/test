package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.exception.InsufficientAuthorityException;
import com.hkt.btu.sd.core.service.bean.SdUserRoleBean;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

public interface SdUserRoleService {
    void updateUserRoleByUserId(String userId, List<String> roleIdList);
    void updateUserRole(String roleId, String roleDesc, String status, Boolean isAbstract);

    List<SdUserRoleBean> getAllUserRole();
    SdUserRoleBean getUserRoleByRoleId(String roleId);

    List<SdUserRoleBean> getCachedRoleAssignMap(String roleId);
    List<SdUserRoleBean> getParentRoleByRoleId(String roleId);

    List<SdUserRoleBean> getUserRoleByUserId(String userId);
    List<SdUserRoleBean> getEligibleUserRoleGrantList();

    void checkUserRole(Set<GrantedAuthority> authorities, List<String> roleEntityList) throws InsufficientAuthorityException;
    boolean checkSameTeamRole(String name, String createBy);
    boolean isEligibleToGrantUserRole(List<String> roleIdList);
}
