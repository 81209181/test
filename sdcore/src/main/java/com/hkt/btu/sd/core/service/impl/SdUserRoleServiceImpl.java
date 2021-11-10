package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import com.hkt.btu.sd.core.dao.entity.SdUserRoleEntity;
import com.hkt.btu.sd.core.dao.mapper.SdUserRoleMapper;
import com.hkt.btu.sd.core.exception.InsufficientAuthorityException;
import com.hkt.btu.sd.core.service.SdCacheService;
import com.hkt.btu.sd.core.service.SdUserRoleService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdUserRoleBean;
import com.hkt.btu.sd.core.service.constant.SdCacheEnum;
import com.hkt.btu.sd.core.service.populator.SdUserRoleBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

public class SdUserRoleServiceImpl implements SdUserRoleService {

    private static final Logger LOG = LogManager.getLogger(SdUserRoleServiceImpl.class);

    private static final List<String> ELIGIBLE_PARENT_ROLE_ID_LIST_OF_PRIMARY_ROLE =
            List.of(SdUserRoleBean.ROLE_ID.OPERATOR, SdUserRoleBean.ROLE_ID.ENGINEER);

    @Resource
    SdUserRoleMapper sdUserRoleMapper;

    @Resource(name = "userService")
    SdUserService userService;
    @Resource(name = "cacheService")
    SdCacheService cacheService;

    @Resource(name = "userRoleBeanPopulator")
    SdUserRoleBeanPopulator sdUserRoleBeanPopulator;

    @Override
    public void reloadCachedRoleTree() {
        LOG.info("reload user role tree.");
        cacheService.reloadCachedObject(SdCacheEnum.USER_ROLE_TREE.getCacheName());
    }

    @Override
    public List<SdUserRoleBean> getParentRoleByRoleId(String roleId) {
        List<SdUserRoleBean> parentRole = getParentRole(getRoleTree().getChildren(), roleId);
        if (CollectionUtils.isEmpty(parentRole)) {
            return null;
        }
        return parentRole;
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

        if (getRoleTree().getRoleId().equals(roleId)) {
            return getRoleTree();
        } else {
            return getChildren(getRoleTree().getChildren(), roleId);
        }
    }

    @Override
    public List<SdUserRoleBean> getUserRoleByUserId(String userId, Boolean checkTeamHead) throws InsufficientAuthorityException {
        List<SdUserRoleBean> results = new LinkedList<>();

        // Get Current User Role
        Set<GrantedAuthority> authorities = userService.getCurrentUserBean().getAuthorities();
        // Get User Role By UserID
        List<SdUserRoleEntity> userRole = sdUserRoleMapper.getUserRoleByUserIdAndStatus(userId, SdUserRoleBean.ACTIVE_ROLE_STATUS);
        // Get RoleId
        List<String> roleIdList = userRole.stream().map(SdUserRoleEntity::getRoleId).collect(Collectors.toList());

        // Check Current User Role
        checkUserRole(authorities, roleIdList, checkTeamHead);

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
    public void checkUserRole(Set<GrantedAuthority> sourceAuthorities, List<String> targetRoleList, boolean checkTeamHead) throws InsufficientAuthorityException {
        // whether check team head authority
        if (checkTeamHead) {
            checkUserRole(sourceAuthorities, targetRoleList);
        } else {
            checkBasicUserRole(sourceAuthorities, targetRoleList);
        }
    }

    private void checkUserRole(Set<GrantedAuthority> sourceAuthorities, List<String> targetRoleList) throws InsufficientAuthorityException {
        if (sourceAuthorities.contains(new SimpleGrantedAuthority(SdUserRoleEntity.SYS_ADMIN))) {
            return;
        }

        // find matching team head authority
        for (GrantedAuthority authority : sourceAuthorities) {
            if (authority instanceof SimpleGrantedAuthority) {
                String roleId = authority.getAuthority();
                if (roleId.contains(SdUserRoleEntity.TEAM_HEAD_INDICATOR)) {
                    String th_roleId = StringUtils.substringAfter(roleId, SdUserRoleEntity.TEAM_HEAD_INDICATOR);
                    boolean flag = targetRoleList.stream().anyMatch(role -> role.equals(th_roleId));
                    if (flag) {
                        return;
                    }
                }
            }
        }

        throw new InsufficientAuthorityException("Insufficient permission.");
    }

    private void checkBasicUserRole(Set<GrantedAuthority> sourceAuthorities, List<String> targetRoleList) throws InsufficientAuthorityException {
        if (sourceAuthorities.contains(new SimpleGrantedAuthority(SdUserRoleEntity.SYS_ADMIN))) {
            return;
        }

        //remove team header head authority checking
        for (GrantedAuthority authority : sourceAuthorities) {
            if (authority instanceof SimpleGrantedAuthority) {
                String roleId = authority.getAuthority();
                boolean flag = targetRoleList.stream().anyMatch(role -> role.equals(roleId));
                if (flag) {
                    return;
                }
            }
        }

        throw new InsufficientAuthorityException("Insufficient permission.");
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
                if (roleId.contains(SdUserRoleEntity.TEAM_HEAD_INDICATOR)) {
                    List<SdUserRoleBean> userRoleByRoleIdInRoleTree =
                            getUserRoleByRoleIdInRoleTree(StringUtils.substringAfter(roleId, SdUserRoleEntity.TEAM_HEAD_INDICATOR));
                    rawEligibleUserRoleList.addAll(userRoleByRoleIdInRoleTree);
                    continue;
                }
                // get user role from role tree
                List<SdUserRoleBean> roleList = getUserRoleByRoleIdInRoleTree(roleId);
                rawEligibleUserRoleList.addAll(roleList);
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
            if (StringUtils.isNotBlank(sdUserRoleBean.getParentRoleId())) {
                if (ELIGIBLE_PARENT_ROLE_ID_LIST_OF_PRIMARY_ROLE.contains(sdUserRoleBean.getParentRoleId())) {
                    sdUserRoleBean.setPrimaryRole(true);
                }
            }
            if (!sdUserRoleBean.isAbstract()) {
                eligibleUserRoleList.add(sdUserRoleBean);
            }
        }
        return eligibleUserRoleList;
    }

    @Override
    public boolean hasUserRole(String roleId) {
        if(StringUtils.isEmpty(roleId)){
            LOG.warn("Empty role ID.");
            return false;
        }

        return userService.hasAnyAuthority(roleId);
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
    @Transactional
    public void changeUserIdInUserUserRole(String oldUserId, String userId, List<SdUserRoleEntity> userRoleByUserId) {
        sdUserRoleMapper.updateUserUserRoleByUserId(oldUserId, userId);
    }

    @Override
    public List<SdUserRoleBean> getTeamHeadRoleList() {
        List<SdUserRoleBean> results = new LinkedList<>();
        List<SdUserRoleEntity> teamHeadRoleList = sdUserRoleMapper.getTeamHeadRoleList(SdUserRoleBean.ROLE_ID.TEAM_HEAD);
        return getSdUserRoleBeans(results, teamHeadRoleList);
    }

    @Override
    public List<List<Object>> getRole4Chart() {
        List<Map<String, Object>> role4Chart = sdUserRoleMapper.getRole4Chart();
        List<List<Object>> result = new ArrayList<>();
        role4Chart.forEach(stringStringMap -> {
            List<Object> objects = List.of(stringStringMap.get("ROLE_NAME"), stringStringMap.get("ROLE_PARENT")==null? "":stringStringMap.get("ROLE_PARENT"));
            result.add(objects);
        });
        return result;
    }

    @Override
    public SdUserRoleBean loadUserRoleTree() {
        LOG.info("load user role tree.");
        SdUserRoleBean userRoleTree = new SdUserRoleBean();
        SdUserRoleEntity topUserRole = sdUserRoleMapper.getTopUserRole();
        sdUserRoleBeanPopulator.populate(topUserRole, userRoleTree);
        List<SdUserRoleEntity> secondRoleList = sdUserRoleMapper.getUserRoleByParentRoleId(userRoleTree.getRoleId());
        List<SdUserRoleBean> allRoleList = new ArrayList<>();
        List<SdUserRoleBean> secondBeanList = populate(secondRoleList);
        userRoleTree.setChildren(secondBeanList);

        for (SdUserRoleBean entity : secondBeanList) {
            if (!SdUserRoleEntity.SYS_ADMIN.equals(entity.getRoleId())) {
                List<SdUserRoleBean> roleList = getAllRoleList(entity.getRoleId());
                entity.setChildren(roleList);
                allRoleList.addAll(roleList);
            }
        }

        // if roleId is System admin.
        for (SdUserRoleBean entity : secondBeanList) {
            if (SdUserRoleEntity.SYS_ADMIN.equals(entity.getRoleId())) {
                entity.setChildren(allRoleList);
            }
        }
        return userRoleTree;
    }

    @Override
    public List<String> getPrimaryRoles() {
        List<SdUserRoleBean> eligibleUserRoleGrantList = getEligibleUserRoleGrantList();
        List<SdUserRoleBean> primaryRoleList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(eligibleUserRoleGrantList)) {
            eligibleUserRoleGrantList.forEach(sdUserRoleBean -> {
                Optional.ofNullable(sdUserRoleBean.getParentRoleId()).ifPresent(parentRoleId -> {
                    if (ELIGIBLE_PARENT_ROLE_ID_LIST_OF_PRIMARY_ROLE.contains(parentRoleId)) {
                        primaryRoleList.add(sdUserRoleBean);
                    }
                });
            });
        }
        return primaryRoleList.stream().sorted(Comparator.comparing(SdUserRoleBean::getRoleDesc)).map(SdUserRoleBean::getRoleId).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserRoleByUserId(String userId, List<String> roleIdList) {
        if (StringUtils.isEmpty(userId)) {
            throw new InvalidInputException("Target user not found.");
        }

        BtuUserBean currentUserBean = userService.getCurrentUserBean();
        checkUserRole(currentUserBean.getAuthorities(), roleIdList, true);

        if (CollectionUtils.isNotEmpty(roleIdList)) {
            // get all existing user role list of userId
            List<SdUserRoleEntity> userRoleList = sdUserRoleMapper.getUserRoleByUserId(userId);

            if (CollectionUtils.isEmpty(userRoleList)) {
                LOG.info("Create userId:" + userId + ", roleIdList:" + roleIdList);
                sdUserRoleMapper.insertUserUserRole(userId, userRoleList.stream().map(SdUserRoleEntity::getRoleId).collect(Collectors.toList()));
            } else {
                List<String> existUserRoleList = userRoleList.stream()
                        .map(SdUserRoleEntity::getRoleId)
                        .collect(Collectors.toList());

                // find which user role to delete
                List<String> toDeleteUserRoleList = new ArrayList<>(existUserRoleList);
                toDeleteUserRoleList.removeAll(roleIdList);
                LOG.info("Deleted userId:" + userId + ", toDeleteUserRoleList:" + toDeleteUserRoleList);

                // find which user role to insert
                roleIdList.removeAll(existUserRoleList);
                LOG.info("Create userId:" + userId + ", toInsertServiceTypeList:" + roleIdList);

                if (CollectionUtils.isNotEmpty(toDeleteUserRoleList)) {
                    sdUserRoleMapper.deleteUserRoleByUserId(userId, toDeleteUserRoleList);
                }

                if (CollectionUtils.isNotEmpty(roleIdList)) {
                    sdUserRoleMapper.insertUserUserRole(userId, roleIdList);
                }
            }
        }
    }

    @Override
    @Transactional
    public void updateUserRole(String roleId, String roleDesc, String status, Boolean isAbstract) {
        String modifyby = userService.getCurrentUserUserId();
        String abstractFlag = isAbstract ? SdUserRoleEntity.IS_ABSTRACT : StringUtils.EMPTY;
        sdUserRoleMapper.updateUserRole(roleId, roleDesc, status, abstractFlag, modifyby);
    }

    /**
     * get cached role tree
     */
    private SdUserRoleBean getRoleTree() {
        return (SdUserRoleBean) cacheService.getCachedObjectByCacheName(SdCacheEnum.USER_ROLE_TREE.getCacheName());
    }


    private List<SdUserRoleBean> getAllRoleList(String roleId) {
        List<SdUserRoleEntity> roleEntityList = sdUserRoleMapper.getUserRoleByParentRoleId(roleId);
        List<SdUserRoleBean> roleList = populate(roleEntityList);
        if (CollectionUtils.isNotEmpty(roleEntityList)) {
            for (SdUserRoleBean entity : roleList) {
                List<SdUserRoleBean> roleBeanList = getAllRoleList(entity.getRoleId());
                entity.setChildren(roleBeanList);
            }
        }
        return roleList;
    }                                                                                                                                                                                               

    /**
     * According to roleId, get user role from role tree
     */
    private List<SdUserRoleBean> getUserRoleByRoleIdInRoleTree(String roleId) {
        List<SdUserRoleBean> userRole = new ArrayList<>();

        if (SdUserRoleEntity.SYS_ADMIN.equals(roleId)) {
            Optional<SdUserRoleBean> adminRole = getRoleTree().getChildren().stream().filter(role -> SdUserRoleEntity.SYS_ADMIN.equals(roleId)).findFirst();
            SdUserRoleBean entity = adminRole.get();
            userRole.add(entity);
            userRole.addAll(entity.getChildren().stream()
                    .filter(role -> role.getStatus().equals(SdUserRoleBean.ACTIVE_ROLE_STATUS))
                    .collect(Collectors.toList()));
            return userRole;
        } else if (getRoleTree().getRoleId().equals(roleId)) {
            userRole.add(getRoleTree());
        } else {
            List<SdUserRoleBean> children = getRoleTree().getChildren();
            if (CollectionUtils.isNotEmpty(children)) {
                Optional<SdUserRoleBean> first = children.stream().filter(role -> role.getRoleId().equals(roleId)).findFirst();
                SdUserRoleBean entity = first.orElse(getChildren(children, roleId));
                if (entity != null) {
                    userRole.add(entity);
                    List<SdUserRoleBean> parentRole = getParentRole(children, entity.getParentRoleId());
                    userRole.addAll(parentRole.stream()
                            .filter(role -> role.getStatus().equals(SdUserRoleBean.ACTIVE_ROLE_STATUS))
                            .collect(Collectors.toList()));
                }
            }
        }
        return userRole;
    }

    /**
     * According to roleId, get user role from roleEntityList.
     */
    private SdUserRoleBean getChildren(List<SdUserRoleBean> roleEntityList, String roleId) {
        if (CollectionUtils.isNotEmpty(roleEntityList)) {
            Optional<SdUserRoleBean> first = roleEntityList
                    .stream()
                    .filter(role -> role.getRoleId().equals(roleId))
                    .findFirst();

            if (first.isEmpty()) {
                for (SdUserRoleBean item : roleEntityList) {
                    if (SdUserRoleEntity.SYS_ADMIN.equals(item.getRoleId())) {
                        continue;
                    }
                    List<SdUserRoleBean> children = item.getChildren();
                    if (CollectionUtils.isNotEmpty(children)) {
                        SdUserRoleBean secondRole = getChildren(children, roleId);
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

    private List<SdUserRoleBean> getParentRole(List<SdUserRoleBean> roleList, String roleId) {
        List<SdUserRoleBean> parentRole = new ArrayList<>();
        for (SdUserRoleBean child : roleList) {
            if (roleId.equals(child.getRoleId())) {
                getParentRole(roleList, child.getParentRoleId());
                parentRole.add(child);
            }
        }
        parentRole.add(getRoleTree());
        return parentRole;
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