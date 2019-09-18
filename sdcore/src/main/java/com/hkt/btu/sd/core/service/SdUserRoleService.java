package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.dao.entity.SdUserRoleEntity;
import com.hkt.btu.sd.core.exception.InsufficientAuthorityException;
import com.hkt.btu.sd.core.service.bean.SdUserRoleBean;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

public interface SdUserRoleService {

    void reloadCachedRoleAssignMap();
    List<SdUserRoleEntity> getParentRoleByRoleId(String roleId); // todo: SdUserRoleEntity --> SdUserRoleBean
    List<SdUserRoleBean> getAllUserRole();
    SdUserRoleBean getUserRoleByRoleId(String roleId);
    List<SdUserRoleBean> getUserRoleByUserId(String userId);
    List<SdUserRoleBean> getEligibleUserRoleGrantList();
    boolean isEligibleToGrantUserRole(List<String> roleIdList);
    void updateUserRoleByUserId(String userId, List<String> roleIdList);
    void updateUserRole(String roleId, String roleDesc, String status);
    void checkUserRole(Set<GrantedAuthority> authorities, List<String> roleEntityList)
            throws InsufficientAuthorityException;
}
