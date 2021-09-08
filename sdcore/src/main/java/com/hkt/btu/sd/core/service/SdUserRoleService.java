package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.dao.entity.SdUserRoleEntity;
import com.hkt.btu.sd.core.exception.InsufficientAuthorityException;
import com.hkt.btu.sd.core.service.bean.SdUserOwnerAuthRoleBean;
import com.hkt.btu.sd.core.service.bean.SdUserRoleBean;
import com.hkt.btu.sd.core.service.bean.SdUserRoleWorkgroupBean;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

public interface SdUserRoleService {
    List<String> getPrimaryRoles();

    void updateUserRoleByUserId(String userId, List<String> roleIdList);
    void updateUserRole(String roleId, String roleDesc, String status, Boolean isAbstract);
    void reloadCachedRoleTree();

    List<SdUserRoleBean> getAllUserRole();
    SdUserRoleBean getUserRoleByRoleId(String roleId);

    List<SdUserRoleBean> getParentRoleByRoleId(String roleId);

    List<SdUserRoleBean> getUserRoleByUserId(String userId, Boolean checkTeamHead);
    List<SdUserRoleBean> getEligibleUserRoleGrantList();

    boolean hasUserRole(String roleId);
    void checkUserRole(Set<GrantedAuthority> authorities, List<String> roleList, boolean checkTeamHead) throws InsufficientAuthorityException;
    boolean checkSameTeamRole(String name, String createBy);
    boolean isEligibleToGrantUserRole(List<String> roleIdList);

    void changeUserIdInUserUserRole(String oldUserId, String userId, List<SdUserRoleEntity> userRoleByUserId);

    List<SdUserRoleBean> getTeamHeadRoleList();

    List<List<Object>> getRole4Chart();

    SdUserRoleBean loadUserRoleTree();

    List<SdUserRoleBean> getRole4UserOwnerAuth();

    List<SdUserOwnerAuthRoleBean> getUserOwnerAuthRoleList();

    String createUserOwnerAuthRole(String ownerId, String authRoleId, String serviceTypeCode);

    String delUserOwnerAuthRole(String ownerId, String authRoleId, String serviceTypeCode);

    List<SdUserRoleWorkgroupBean> getUserRoleWorkgroupList();

    String delUserRoleWorkgroup(String roleId, String workgroup);

    String createUserRoleWorkgroup(String roleId, String workgroup);

    List<String> getWorkgroupList();
}
