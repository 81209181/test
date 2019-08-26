package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.dao.entity.SdUserRoleEntity;
import com.hkt.btu.sd.core.dao.mapper.SdUserRoleMapper;
import com.hkt.btu.sd.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.SdConfigParamService;
import com.hkt.btu.sd.core.service.SdUserRoleService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdUserRoleBean;
import com.hkt.btu.sd.core.service.populator.SdUserRoleBeanPopulator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

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
        List<SdUserRoleEntity> allUserRole = sdUserRoleMapper.getAllUserRole();
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

        // get All User Role
        List<SdUserRoleBean> allUserRoles = getAllUserRole();
        if (CollectionUtils.isEmpty(allUserRoles)) {
            return null;
        }

        // extract eligible roles of current user role
        List<String> eligibleUserRoleIdList = new LinkedList<>();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority instanceof SimpleGrantedAuthority) {
                String userRoleId = grantedAuthority.getAuthority();
                String mappedRoleIds = configParamService.getString(SdConfigParamEntity.USER_ROLE_CREATE_MAPPING.CONFIG_ROLE, userRoleId);
                if (StringUtils.isEmpty(mappedRoleIds)) {
                    continue;
                }

                String[] splitGroupIds = mappedRoleIds.split(",");
                List<String> splitGroupIdList = Arrays.asList(splitGroupIds);
                eligibleUserRoleIdList.addAll(splitGroupIdList);
            }
        }

        // add eligible bean to result
        List<SdUserRoleBean> result = new LinkedList<>();
        for (SdUserRoleBean sdUserRoleBean : allUserRoles) {
            String userRoleId = sdUserRoleBean.getRoleId();
            if (eligibleUserRoleIdList.contains(userRoleId)) {
                result.add(sdUserRoleBean);
            }
        }

        return result;
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
