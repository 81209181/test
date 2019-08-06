package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.dao.entity.BtuUserGroupEntity;
import com.hkt.btu.common.core.dao.mapper.BtuUserGroupMapper;
import com.hkt.btu.common.core.exception.InsufficientAuthorityException;
import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.BtuConfigParamService;
import com.hkt.btu.common.core.service.BtuUserGroupService;
import com.hkt.btu.common.core.service.BtuUserService;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.core.service.bean.BtuUserGroupBean;
import com.hkt.btu.common.core.service.populator.BtuUserGroupBeanPopulator;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class BtuUserGroupServiceImpl implements BtuUserGroupService {


    @Resource
    BtuUserGroupMapper sdUserGroupMapper;

    @Resource(name = "userGroupBeanPopulator")
    BtuUserGroupBeanPopulator userGroupBeanPopulator;

    @Resource(name = "configParamService")
    BtuConfigParamService configParamService;
    @Resource(name = "userService")
    BtuUserService userService;

    @Override
    public List<BtuUserGroupBean> getAllUserGroup() {
        List<BtuUserGroupBean> result = new LinkedList<>();

        List<BtuUserGroupEntity> sdUserGroupEntityList = sdUserGroupMapper.getAllUserGroup();
        if (CollectionUtils.isEmpty(sdUserGroupEntityList)) {
            return result;
        }

        for (BtuUserGroupEntity entity : sdUserGroupEntityList) {
            BtuUserGroupBean bean = new BtuUserGroupBean();
            userGroupBeanPopulator.populate(entity, bean);
            result.add(bean);
        }

        return result;

    }

    @Override
    public List<BtuUserGroupBean> getEligibleUserGroupGrantList() {
        // get current user group
        BtuUser currentUser = userService.getCurrentUser();
        Collection<GrantedAuthority> authorities = currentUser == null ? null : currentUser.getAuthorities();
        if (CollectionUtils.isEmpty(authorities)) {
            return null;
        }

        // get all user group
        List<BtuUserGroupBean> allUserGroup = getAllUserGroup();
        if (CollectionUtils.isEmpty(allUserGroup)) {
            return null;
        }

        // extract eligible groups of current user group
        List<String> eligibleUserGroupIdList = new LinkedList<>();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority instanceof SimpleGrantedAuthority) {
                String userGroupId = grantedAuthority.getAuthority();
                String mappedGroupIds = configParamService.getString(BtuConfigParamEntity.USER_GROUP_CREATE_MAPPING.CONFIG_GROUP, userGroupId);
                if (StringUtils.isEmpty(mappedGroupIds)) {
                    continue;
                }

                String[] splitGroupIds = mappedGroupIds.split(",");
                List<String> splitGroupIdList = Arrays.asList(splitGroupIds);
                eligibleUserGroupIdList.addAll(splitGroupIdList);
            }
        }

        // add eligible bean to result
        List<BtuUserGroupBean> result = new LinkedList<>();
        for (BtuUserGroupBean sdUserGroupBean : allUserGroup) {
            String userGroupId = sdUserGroupBean.getGroupId();
            if (eligibleUserGroupIdList.contains(userGroupId)) {
                result.add(sdUserGroupBean);
            }
        }
        return result;

    }

    @Override
    public boolean isEligibleToGrantUserGroup(List<String> toGrantGroupIdList) {
        if (CollectionUtils.isEmpty(toGrantGroupIdList)) {
            return true;
        }

        // get user group which can be granted by current user
        List<BtuUserGroupBean> eligibleUserGroupList = getEligibleUserGroupGrantList();
        if (CollectionUtils.isEmpty(eligibleUserGroupList)) {
            return false;
        }

        List<String> eligibleUserGroupIdList = new LinkedList<>();
        for (BtuUserGroupBean sdUserGroupBean : eligibleUserGroupList) {
            eligibleUserGroupIdList.add(sdUserGroupBean.getGroupId());
        }

        // check each to-be-granted user group
        for (String userGroupId : toGrantGroupIdList) {
            if (!eligibleUserGroupIdList.contains(userGroupId)) {
                return false;
            }
        }
        return true;

    }

    @Override
    public void updateUserGroup(Integer userId, Boolean updateIsAdmin, Boolean updateIsUser, Boolean updateIsCAdmin, Boolean updateIsCUser, BtuUserBean modifier) throws InsufficientAuthorityException, InvalidInputException {
        if (userId == null) {
            throw new InvalidInputException("Target user not found.");
        }

        // check user has auth to update
        if (updateIsAdmin != null || updateIsUser != null) {
            if (!userService.hasAnyAuthority(BtuUserGroupEntity.GROUP_ID.ADMIN)) {
                throw new InsufficientAuthorityException("Insufficient authority to modify internal user.");
            }
        }
        if (updateIsCAdmin != null || updateIsCUser != null) {
            if (!userService.hasAnyAuthority(BtuUserGroupEntity.GROUP_ID.C_ADMIN, BtuUserGroupEntity.GROUP_ID.ADMIN)) {
                throw new InsufficientAuthorityException("Insufficient authority to modify external user.");
            }
        }

        Integer modifierUserId = modifier.getUserId();

        if (updateIsAdmin != null) {
            sdUserGroupMapper.deleteUserUserGroup(userId, BtuUserGroupEntity.GROUP_ID.ADMIN);
            if (BooleanUtils.isTrue(updateIsAdmin)) {
                sdUserGroupMapper.insertUserUserGroup(userId, BtuUserGroupEntity.GROUP_ID.ADMIN, modifierUserId);
            }
        }

        if (updateIsUser != null) {
            sdUserGroupMapper.deleteUserUserGroup(userId, BtuUserGroupEntity.GROUP_ID.USER);
            if (BooleanUtils.isTrue(updateIsUser)) {
                sdUserGroupMapper.insertUserUserGroup(userId, BtuUserGroupEntity.GROUP_ID.USER, modifierUserId);
            }
        }

        if (updateIsCAdmin != null) {
            sdUserGroupMapper.deleteUserUserGroup(userId, BtuUserGroupEntity.GROUP_ID.C_ADMIN);
            if (BooleanUtils.isTrue(updateIsCAdmin)) {
                sdUserGroupMapper.insertUserUserGroup(userId, BtuUserGroupEntity.GROUP_ID.C_ADMIN, modifierUserId);
            }
        }

        if (updateIsCUser != null) {
            sdUserGroupMapper.deleteUserUserGroup(userId, BtuUserGroupEntity.GROUP_ID.C_USER);
            if (BooleanUtils.isTrue(updateIsCUser)) {
                sdUserGroupMapper.insertUserUserGroup(userId, BtuUserGroupEntity.GROUP_ID.C_USER, modifierUserId);
            }
        }

    }
}
