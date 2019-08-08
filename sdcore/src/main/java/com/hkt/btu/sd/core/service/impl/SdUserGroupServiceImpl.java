package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.dao.entity.SdUserGroupEntity;
import com.hkt.btu.sd.core.dao.mapper.SdUserGroupMapper;
import com.hkt.btu.sd.core.exception.InsufficientAuthorityException;
import com.hkt.btu.sd.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.SdConfigParamService;
import com.hkt.btu.sd.core.service.SdUserGroupService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdUserBean;
import com.hkt.btu.sd.core.service.bean.SdUserGroupBean;
import com.hkt.btu.sd.core.service.populator.SdUserGroupBeanPopulator;
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

public class SdUserGroupServiceImpl implements SdUserGroupService {


    @Resource
    SdUserGroupMapper sdUserGroupMapper;

    @Resource(name = "userGroupBeanPopulator")
    SdUserGroupBeanPopulator sdUserGroupBeanPopulator;

    @Resource(name = "sdConfigParamService")
    SdConfigParamService sdConfigParamService;
    @Resource(name = "userService")
    SdUserService sdUserService;

    @Override
    public List<SdUserGroupBean> getAllUserGroup() {
        List<SdUserGroupBean> result = new LinkedList<>();

        List<SdUserGroupEntity> sdUserGroupEntityList = sdUserGroupMapper.getAllUserGroup();
        if(CollectionUtils.isEmpty(sdUserGroupEntityList)){
            return result;
        }

        for(SdUserGroupEntity entity : sdUserGroupEntityList){
            SdUserGroupBean bean = new SdUserGroupBean();
            sdUserGroupBeanPopulator.populate(entity, bean);
            result.add(bean);
        }

        return result;
    }

    @Override
    public List<SdUserGroupBean> getEligibleUserGroupGrantList() {
        // get current user group
        BtuUser currentUser = sdUserService.getCurrentUser();
        Collection<GrantedAuthority> authorities = currentUser==null ? null : currentUser.getAuthorities();
        if(CollectionUtils.isEmpty(authorities)){
            return null;
        }

        // get all user group
        List<SdUserGroupBean> allUserGroup = getAllUserGroup();
        if(CollectionUtils.isEmpty(allUserGroup)){
            return null;
        }

        // extract eligible groups of current user group
        List<String> eligibleUserGroupIdList = new LinkedList<>();
        for(GrantedAuthority grantedAuthority : authorities){
            if(grantedAuthority instanceof SimpleGrantedAuthority){
                String userGroupId = grantedAuthority.getAuthority();
                String mappedGroupIds = sdConfigParamService.getString(SdConfigParamEntity.USER_GROUP_CREATE_MAPPING.CONFIG_GROUP, userGroupId);
                if(StringUtils.isEmpty(mappedGroupIds)){
                    continue;
                }

                String [] splitGroupIds = mappedGroupIds.split(",");
                List<String> splitGroupIdList = Arrays.asList(splitGroupIds);
                eligibleUserGroupIdList.addAll(splitGroupIdList);
            }
        }

        // add eligible bean to result
        List<SdUserGroupBean> result = new LinkedList<>();
        for(SdUserGroupBean sdUserGroupBean : allUserGroup){
            String userGroupId = sdUserGroupBean.getGroupId();
            if(eligibleUserGroupIdList.contains(userGroupId)){
                result.add(sdUserGroupBean);
            }
        }
        return result;
    }

    @Override
    public boolean isEligibleToGrantUserGroup(List<String> toGrantGroupIdList) {
        if(CollectionUtils.isEmpty(toGrantGroupIdList)){
            return true;
        }

        // get user group which can be granted by current user
        List<SdUserGroupBean> eligibleUserGroupList = getEligibleUserGroupGrantList();
        if(CollectionUtils.isEmpty(eligibleUserGroupList)){
            return false;
        }

        List<String> eligibleUserGroupIdList = new LinkedList<>();
        for(SdUserGroupBean sdUserGroupBean :  eligibleUserGroupList){
            eligibleUserGroupIdList.add(sdUserGroupBean.getGroupId());
        }

        // check each to-be-granted user group
        for(String userGroupId : toGrantGroupIdList){
            if(! eligibleUserGroupIdList.contains(userGroupId)){
                return false;
            }
        }
        return true;
    }

    @Override
    public void updateUserGroup(Integer userId, Boolean updateIsAdmin, Boolean updateIsUser,
                                Boolean updateIsCAdmin, Boolean updateIsCUser, SdUserBean modifier)
            throws InsufficientAuthorityException, InvalidInputException {
        if(userId==null){
            throw new InvalidInputException("Target user not found.");
        }

        // check user has auth to update
        if(updateIsAdmin!=null || updateIsUser!=null){
            if(! sdUserService.hasAnyAuthority(SdUserGroupEntity.GROUP_ID.ADMIN)){
                throw new InsufficientAuthorityException("Insufficient authority to modify internal user.");
            }
        }
        if(updateIsCAdmin!=null || updateIsCUser!=null){
            if(! sdUserService.hasAnyAuthority(SdUserGroupEntity.GROUP_ID.C_ADMIN, SdUserGroupEntity.GROUP_ID.ADMIN)){
                throw new InsufficientAuthorityException("Insufficient authority to modify external user.");
            }
        }

        Integer modifierUserId = modifier.getUserId();

        if(updateIsAdmin!=null){
            sdUserGroupMapper.deleteUserUserGroup(userId, SdUserGroupEntity.GROUP_ID.ADMIN);
            if(BooleanUtils.isTrue(updateIsAdmin)){
                sdUserGroupMapper.insertUserUserGroup(userId, SdUserGroupEntity.GROUP_ID.ADMIN, modifierUserId);
            }
        }

        if(updateIsUser!=null){
            sdUserGroupMapper.deleteUserUserGroup(userId, SdUserGroupEntity.GROUP_ID.USER);
            if(BooleanUtils.isTrue(updateIsUser)){
                sdUserGroupMapper.insertUserUserGroup(userId, SdUserGroupEntity.GROUP_ID.USER, modifierUserId);
            }
        }

        if(updateIsCAdmin!=null){
            sdUserGroupMapper.deleteUserUserGroup(userId, SdUserGroupEntity.GROUP_ID.C_ADMIN);
            if(BooleanUtils.isTrue(updateIsCAdmin)){
                sdUserGroupMapper.insertUserUserGroup(userId, SdUserGroupEntity.GROUP_ID.C_ADMIN, modifierUserId);
            }
        }

        if(updateIsCUser!=null){
            sdUserGroupMapper.deleteUserUserGroup(userId, SdUserGroupEntity.GROUP_ID.C_USER);
            if(BooleanUtils.isTrue(updateIsCUser)){
                sdUserGroupMapper.insertUserUserGroup(userId, SdUserGroupEntity.GROUP_ID.C_USER, modifierUserId);
            }
        }

    }


}
