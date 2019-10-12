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
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

public class SdUserRoleServiceImpl implements SdUserRoleService {

    private static final SdUserRoleEntity ROLE_TREE = new SdUserRoleEntity();

    @Resource
    SdUserRoleMapper sdUserRoleMapper;

    @Resource(name = "userService")
    SdUserService userService;

    @Resource(name = "userRoleBeanPopulator")
    SdUserRoleBeanPopulator sdUserRoleBeanPopulator;

    @Override
    public void reloadCachedRoleTree() {
        getRoleTree();
    }

    @Override
    public List<SdUserRoleBean> getParentRoleByRoleId(String roleId) {

        checkRoleTree();

        List<SdUserRoleEntity> parentRole = getParentRole(ROLE_TREE.getChildren(), roleId);

        if (CollectionUtils.isEmpty(parentRole)) {
            return null;
        }

        return parentRole.stream().map(entity -> {
            SdUserRoleBean bean = new SdUserRoleBean();
            sdUserRoleBeanPopulator.populate(entity, bean);
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SdUserRoleBean> getAllUserRole() {
        List<SdUserRoleBean> results = new LinkedList<>();
        List<SdUserRoleEntity> allUserRole = sdUserRoleMapper.getAllUserRole(null);
        return getSdUserRoleBeans(results, allUserRole);
    }

    @Override
    public SdUserRoleBean getUserRoleByRoleId(String roleId) {
        if (StringUtils.isEmpty(roleId)) {
            return null;
        }

        SdUserRoleEntity children = new SdUserRoleEntity();

        checkRoleTree();

        if (ROLE_TREE.getRoleId().equals(roleId)) {
            children = ROLE_TREE;
        } else {
            children = getChildren(ROLE_TREE.getChildren(), roleId);
        }

        if (children == null) {
            return null;
        }

        // construct bean
        SdUserRoleBean userBean = new SdUserRoleBean();
        sdUserRoleBeanPopulator.populate(children, userBean);

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

        checkRoleTree();

        // extract eligible roles of current user role
        List<SdUserRoleBean> rawEligibleUserRoleList = new LinkedList<>();
        for (GrantedAuthority authority : authorities) {
            if (authority instanceof SimpleGrantedAuthority) {
                String roleId = authority.getAuthority();
                // get user role from role tree
                List<SdUserRoleEntity> roleList = getUserRoleByRoleIdInRoleTree(roleId);
                rawEligibleUserRoleList.addAll(populate(roleList));
            }
        }
        if (CollectionUtils.isEmpty(rawEligibleUserRoleList)) {
            return null;
        }

        // remove duplicate role
        rawEligibleUserRoleList = rawEligibleUserRoleList.stream().distinct().collect(Collectors.toList());

        // remove abstract role
        List<SdUserRoleBean> eligibleUserRoleList = new LinkedList<>();
        for (SdUserRoleBean sdUserRoleBean : rawEligibleUserRoleList) {
            if (!sdUserRoleBean.isAbstract()) {
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

    /**
     *   Initial role tree
     */
    private void getRoleTree() {
        SdUserRoleEntity topUserRole = sdUserRoleMapper.getTopUserRole();
        BeanUtils.copyProperties(topUserRole, ROLE_TREE);
        List<SdUserRoleEntity> secondRoleList = sdUserRoleMapper.getUserRoleByParentRoleId(ROLE_TREE.getRoleId());
        List<SdUserRoleEntity> allRoleList = new ArrayList<>();
        ROLE_TREE.setChildren(secondRoleList);

        for (SdUserRoleEntity entity : secondRoleList) {
            List<SdUserRoleEntity> roleList = getAllRoleList(entity.getRoleId());
            entity.setChildren(roleList);
            allRoleList.addAll(roleList);
        }

        // if roleId is System admin.
        for (SdUserRoleEntity entity : secondRoleList) {
            if (SdUserRoleEntity.SYS_ADMIN.equals(entity.getRoleId())) {
                entity.setChildren(allRoleList);
            }
        }
    }

    /**
     *  Get all user role from database.
     * @param roleId
     * @return
     */
    private List<SdUserRoleEntity> getAllRoleList(String roleId) {
        List<SdUserRoleEntity> roleList = sdUserRoleMapper.getUserRoleByParentRoleId(roleId);
        if (CollectionUtils.isNotEmpty(roleList)) {
            for (SdUserRoleEntity entity : roleList) {
                entity.setChildren(getAllRoleList(entity.getRoleId()));
            }
        }
        return roleList;
    }

    /**
     *  According to roleId, get user role from role tree
     * @param roleId
     * @return
     */
    private List<SdUserRoleEntity> getUserRoleByRoleIdInRoleTree(String roleId) {
        List<SdUserRoleEntity> userRole = new ArrayList<>();

        checkRoleTree();

        if (SdUserRoleEntity.SYS_ADMIN.equals(roleId)) {
            Optional<SdUserRoleEntity> adminRole = ROLE_TREE.getChildren().stream().filter(role -> SdUserRoleEntity.SYS_ADMIN.equals(roleId)).findFirst();
            SdUserRoleEntity entity = adminRole.get();
            userRole.add(entity);
            userRole.addAll(entity.getChildren().stream()
                    .filter(role -> role.getStatus().equals(SdUserRoleEntity.ACTIVE_ROLE_STATUS))
                    .collect(Collectors.toList()));
            return userRole;
        } else if (ROLE_TREE.getRoleId().equals(roleId)) {
            userRole.add(ROLE_TREE);
        } else {
            List<SdUserRoleEntity> children = ROLE_TREE.getChildren();
            if (CollectionUtils.isNotEmpty(children)) {
                Optional<SdUserRoleEntity> first = children.stream().filter(role -> role.getRoleId().equals(roleId)).findFirst();
                SdUserRoleEntity entity = first.orElse(getChildren(children, roleId));
                if (entity != null) {
                    userRole.add(entity);
                    List<SdUserRoleEntity> parentRole = getParentRole(children, entity.getParentRoleId());
                    userRole.addAll(parentRole.stream()
                            .filter(role -> role.getStatus().equals(SdUserRoleEntity.ACTIVE_ROLE_STATUS))
                            .collect(Collectors.toList()));
                }
            }
        }
        return userRole;
    }

    /**
     *  According to roleId, get user role from roleEntityList.
     * @param roleEntityList
     * @param roleId
     * @return
     */
    private SdUserRoleEntity getChildren(List<SdUserRoleEntity> roleEntityList, String roleId) {
        if (CollectionUtils.isNotEmpty(roleEntityList)) {
            Optional<SdUserRoleEntity> first = roleEntityList
                    .stream()
                    .filter(role -> role.getRoleId().equals(roleId))
                    .findFirst();

            if (first.isEmpty()) {
                for (SdUserRoleEntity item : roleEntityList) {
                    if (SdUserRoleEntity.SYS_ADMIN.equals(item.getRoleId())) {
                        continue;
                    }
                    List<SdUserRoleEntity> children = item.getChildren();
                    if (CollectionUtils.isNotEmpty(children)) {
                        SdUserRoleEntity secondRole = getChildren(children, roleId);
                        if (secondRole != null) {
                            return secondRole;
                        }
                    }
                }
            } else {
                return first.get();
            }
        }
        return null;
    }

    private List<SdUserRoleEntity> getParentRole(List<SdUserRoleEntity> roleList, String roleId) {
        List<SdUserRoleEntity> parentRole = new ArrayList<>();
        for (SdUserRoleEntity child : roleList) {
            if (roleId.equals(child.getRoleId())) {
                getParentRole(roleList, child.getParentRoleId());
                parentRole.add(child);
            }
        }
        parentRole.add(ROLE_TREE);
        return parentRole;
    }

    private void checkRoleTree() {
        if (ROLE_TREE == null) {
            getRoleTree();
        }
    }

    private List<SdUserRoleBean> populate(List<SdUserRoleEntity> roleEntityList) {
        if (CollectionUtils.isEmpty(roleEntityList)) {
            return null;
        }

        return roleEntityList.stream().map(entity -> {
          SdUserRoleBean bean = new SdUserRoleBean();
          sdUserRoleBeanPopulator.populate(entity, bean);
          return bean;
        }).collect(Collectors.toList());
    }


}