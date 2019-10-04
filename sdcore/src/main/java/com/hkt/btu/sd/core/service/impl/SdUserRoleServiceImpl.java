package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import com.hkt.btu.sd.core.dao.entity.SdUserRoleEntity;
import com.hkt.btu.sd.core.dao.mapper.SdUserRoleMapper;
import com.hkt.btu.sd.core.exception.InsufficientAuthorityException;
import com.hkt.btu.sd.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.SdUserRoleService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdUserRoleBean;
import com.hkt.btu.sd.core.service.populator.SdUserRoleBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

public class SdUserRoleServiceImpl implements SdUserRoleService {

    // key: role id, value: list of role id that can be assigned by the key role id
    private static final Map<String, List<SdUserRoleBean>> ROLE_ASSIGN_MAP = new HashMap<>();

    @Resource
    SdUserRoleMapper sdUserRoleMapper;

    @Resource(name = "userService")
    SdUserService userService;

    @Resource(name = "userRoleBeanPopulator")
    SdUserRoleBeanPopulator sdUserRoleBeanPopulator;

    private void reloadCachedRoleAssignMap() {
        List<String> teamHeadRoleIdList = sdUserRoleMapper
                .getTeamHeadList(SdUserRoleEntity.TEAM_HEAD_INDICATOR)
                .stream()
                .map(SdUserRoleEntity::getRoleId)
                .collect(Collectors.toList());
        for (String roleId : teamHeadRoleIdList) {
            List<SdUserRoleEntity> eligibleRoleEntityList = sdUserRoleMapper
                    .getEligibleRolesByCurrentUserRole(roleId, SdUserRoleEntity.ACTIVE_ROLE_STATUS);
            List<SdUserRoleBean> eligibleRoleBeanList = getSdUserRoleBeans(new LinkedList<>(), eligibleRoleEntityList);
            ROLE_ASSIGN_MAP.put(roleId, eligibleRoleBeanList);
        }
    }

    @Override
    public List<SdUserRoleBean> getCachedRoleAssignMap(String roleId) {
        if (MapUtils.isEmpty(ROLE_ASSIGN_MAP)) {
            reloadCachedRoleAssignMap();
        }
        return ROLE_ASSIGN_MAP.get(roleId);
    }

    @Override
    public List<SdUserRoleBean> getParentRoleByRoleId(String roleId) {
        List<SdUserRoleBean> roleEntityList = new LinkedList<>();
        List<SdUserRoleEntity> parentRoleByRoleId = sdUserRoleMapper.getParentRoleByRoleId(roleId);
        for (SdUserRoleEntity role : parentRoleByRoleId) {
            if (role.getStatus().equals(SdUserRoleEntity.ACTIVE_ROLE_STATUS)) {
                if (StringUtils.isNotEmpty(role.getParentRoleId())) {
                    List<SdUserRoleBean> parentRoleList = getParentRoleByRoleId(role.getParentRoleId());
                    roleEntityList.addAll(parentRoleList);
                }
                SdUserRoleBean bean = new SdUserRoleBean();
                sdUserRoleBeanPopulator.populate(role, bean);
                roleEntityList.add(bean);
            }
        }
        return roleEntityList;
    }

    @Override
    public List<SdUserRoleBean> getAllUserRole() {
        List<SdUserRoleBean> results = new LinkedList<>();
        List<SdUserRoleEntity> allUserRole = sdUserRoleMapper.getAllUserRole(null);
        return getSdUserRoleBeans(results, allUserRole);
    }

    @Override
    public SdUserRoleBean getUserRoleByRoleId(String roleId) {
        if (roleId == null) {
            return null;
        }

        // get user data
        SdUserRoleEntity sdUserRoleEntity = sdUserRoleMapper.getUserRoleByRoleId(roleId);
        if (sdUserRoleEntity == null) {
            return null;
        }

        // construct bean
        SdUserRoleBean userBean = new SdUserRoleBean();
        sdUserRoleBeanPopulator.populate(sdUserRoleEntity, userBean);

        return userBean;
    }

    @Override
    public List<SdUserRoleBean> getUserRoleByUserId(String userId) throws InsufficientAuthorityException {
        List<SdUserRoleBean> results = new LinkedList<>();

        // Get Current User Role
        Set<GrantedAuthority> authorities = userService.getCurrentUserBean().getAuthorities();
        // Get User Role By UserID
        List<SdUserRoleEntity> userRole = sdUserRoleMapper.getUserRoleByUserIdAndStatus(userId, SdUserRoleEntity.ACTIVE_ROLE_STATUS);
        // Get RoleId
        List<String> roleIdList = userRole.stream().map(SdUserRoleEntity::getRoleId).collect(Collectors.toList());

        // Check Current User Role
        checkUserRole(authorities, roleIdList);

        return getSdUserRoleBeans(results, userRole);
    }

    private List<SdUserRoleBean> getSdUserRoleBeans(List<SdUserRoleBean> results, List<SdUserRoleEntity> userRole) {
        if (CollectionUtils.isEmpty(userRole)) {
            return null;
        }
        for (SdUserRoleEntity entity : userRole) {
            SdUserRoleBean bean = new SdUserRoleBean();
            sdUserRoleBeanPopulator.populate(entity, bean);
            results.add(bean);
        }
        return results;
    }

    @Override
    public void checkUserRole(Set<GrantedAuthority> authorities, List<String> roleEntityList) throws InsufficientAuthorityException {
        if (authorities.contains(new SimpleGrantedAuthority(SdUserRoleEntity.SYS_ADMIN))) {
            return;
        }

        // find matching team head authority
        for (GrantedAuthority authority : authorities) {
            if (authority instanceof SimpleGrantedAuthority) {
                String roleId = authority.getAuthority();
                if (roleId.contains(SdUserRoleEntity.TEAM_HEAD_INDICATOR)) {
                    String th_roleId = StringUtils.substringAfter(roleId, SdUserRoleEntity.TEAM_HEAD_INDICATOR);
                    boolean flag = roleEntityList.stream().anyMatch(role -> role.equals(th_roleId));
                    if (flag) {
                        return;
                    }
                }
            }
        }

        throw new InsufficientAuthorityException("You are no permission.");
    }

    @Override
    public boolean checkSameTeamRole(String loginId, String createBy) {
        List<String> loginUser = sdUserRoleMapper.getTeamRoleByUserId(loginId);
        List<String> createByUser = sdUserRoleMapper.getTeamRoleByUserId(createBy);
        if (loginUser.isEmpty()) {
            return false;
        }
        return createByUser.stream().anyMatch(loginUser::contains);
    }


    @Override
    public List<SdUserRoleBean> getEligibleUserRoleGrantList() {
        // get Current User Role
        BtuUser currentUser = userService.getCurrentUser();
        Collection<GrantedAuthority> authorities = currentUser == null ? null : currentUser.getAuthorities();
        if (CollectionUtils.isEmpty(authorities)) {
            return null;
        }

        // extract eligible roles of current user role
        List<SdUserRoleBean> rawEligibleUserRoleList = new LinkedList<>();
        for (GrantedAuthority authority : authorities) {
            if (authority instanceof SimpleGrantedAuthority) {
                String roleId = authority.getAuthority();
                if (roleId.equals(SdUserRoleEntity.SYS_ADMIN)) {
                    rawEligibleUserRoleList = getAllUserRole();
                    break;
                }
                if (roleId.contains(SdUserRoleEntity.TEAM_HEAD_INDICATOR)) {
                    List<SdUserRoleBean> eligibleRoleListOfTeamHead = getCachedRoleAssignMap(roleId);
                    rawEligibleUserRoleList.addAll(eligibleRoleListOfTeamHead);
                }
            }
        }
        if (CollectionUtils.isEmpty(rawEligibleUserRoleList)) {
            return null;
        }

        // remove duplicate role
        rawEligibleUserRoleList = rawEligibleUserRoleList.stream().distinct().collect(Collectors.toList());

        // remove abstract role
        List<SdUserRoleBean> eligibleUserRoleList = new LinkedList<>();
        for(SdUserRoleBean sdUserRoleBean : rawEligibleUserRoleList){
            if(!sdUserRoleBean.isAbstract()){
                eligibleUserRoleList.add(sdUserRoleBean);
            }
        }
        return eligibleUserRoleList;
    }


    @Override
    public boolean isEligibleToGrantUserRole(List<String> toGrantRoleIdList) {
        if (CollectionUtils.isEmpty(toGrantRoleIdList)) {
            return true;
        }

        List<SdUserRoleBean> eligibleUserRoleGrantList = getEligibleUserRoleGrantList();
        if (CollectionUtils.isEmpty(eligibleUserRoleGrantList)) {
            return false;
        }

        List<String> eligibleUserRoleIdGrantList = new LinkedList<>();
        for (SdUserRoleBean userRoleBean : eligibleUserRoleGrantList) {
            eligibleUserRoleIdGrantList.add(userRoleBean.getRoleId());
        }

        for (String roleId : toGrantRoleIdList) {
            if (!eligibleUserRoleIdGrantList.contains(roleId)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void updateUserRoleByUserId(String userId, List<String> roleIdList) {
        if (StringUtils.isEmpty(userId)) {
            throw new InvalidInputException("Target user not found.");
        }

        if (!CollectionUtils.isEmpty(roleIdList)) {
            sdUserRoleMapper.deleteUserRoleByUserId(userId);
            for (String roleId : roleIdList) {
                sdUserRoleMapper.insertUserUserRole(userId, roleId);
            }
        }
    }

    @Override
    public void updateUserRole(String roleId, String roleDesc, String status, Boolean isAbstract) {
        String modifyby = userService.getCurrentUserUserId();
        String abstractFlag = isAbstract ? SdUserRoleEntity.IS_ABSTRACT : StringUtils.EMPTY;
        sdUserRoleMapper.updateUserRole(roleId, roleDesc, status, abstractFlag, modifyby);
    }
}
