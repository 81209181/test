package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import com.hkt.btu.sd.core.dao.entity.SdUserRoleEntity;
import com.hkt.btu.sd.core.dao.mapper.SdUserRoleMapper;
import com.hkt.btu.sd.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.SdConfigParamService;
import com.hkt.btu.sd.core.service.SdUserRoleService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdUserRoleBean;
import com.hkt.btu.sd.core.service.populator.SdUserRoleBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

public class SdUserRoleServiceImpl implements SdUserRoleService {

    @Resource
    SdUserRoleMapper sdUserRoleMapper;

    @Resource(name = "userService")
    SdUserService userService;

    @Resource(name = "configParamService")
    SdConfigParamService configParamService;

    @Resource(name = "userRolePopulator")
    SdUserRoleBeanPopulator sdUserRoleBeanPopulator;

    @Override
    public List<SdUserRoleBean> getAllUserRole() {
        List<SdUserRoleBean> results = new LinkedList<>();
        List<SdUserRoleEntity> allUserRole = sdUserRoleMapper.getAllUserRole(SdUserRoleEntity.ACTIVE_ROLE_STATUS);
        return getSdUserRoleBeans(results, allUserRole);
    }

    @Override
    public List<SdUserRoleBean> getUserRoleByUserId(String userId) {
        List<SdUserRoleBean> results = new LinkedList<>();
        List<SdUserRoleEntity> userRole = sdUserRoleMapper.getUserRoleByUserId(userId);
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
    public List<SdUserRoleBean> getEligibleUserRoleGrantList() {
        // get Current User Role
        BtuUser currentUser = userService.getCurrentUser();
        Collection<GrantedAuthority> authorities = currentUser == null ? null : currentUser.getAuthorities();
        if (CollectionUtils.isEmpty(authorities)) {
            return null;
        }

        // extract eligible roles of current user role
        List<SdUserRoleEntity> userRoleEntityList = new LinkedList<>();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority instanceof SimpleGrantedAuthority) {
                String userRoleId = grantedAuthority.getAuthority();
                if (userRoleId.contains(SdUserRoleEntity.TEAM_HEAD_INDICATOR)) {
                    List<SdUserRoleEntity> eligibleRolesByCurrentUserRole =
                            sdUserRoleMapper.getEligibleRolesByCurrentUserRole(userRoleId, SdUserRoleEntity.ACTIVE_ROLE_STATUS);
                    userRoleEntityList.addAll(eligibleRolesByCurrentUserRole);
                } else if (userRoleId.equals(SdUserRoleEntity.SYS_ADMIN)) {
                    userRoleEntityList = sdUserRoleMapper.getAllUserRole(SdUserRoleEntity.ACTIVE_ROLE_STATUS);
                }
            }
        }


        if (CollectionUtils.isNotEmpty(userRoleEntityList)) {
            List<SdUserRoleBean> eligibleUserRoleList = userRoleEntityList.stream().map(entity -> {
                SdUserRoleBean bean = new SdUserRoleBean();
                sdUserRoleBeanPopulator.populate(entity, bean);
                return bean;
            }).distinct().collect(Collectors.toList());

            return eligibleUserRoleList;
        }

        return null;
    }

    @Override
    public boolean isEligibleToGrantUserRole(List<String> roleIdList) {
        if (CollectionUtils.isEmpty(roleIdList)) {
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

        for (String roleId : roleIdList) {
            if (!eligibleUserRoleIdGrantList.contains(roleId)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void updateUserRole(String userId, List<String> roleIdList) {
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
}
