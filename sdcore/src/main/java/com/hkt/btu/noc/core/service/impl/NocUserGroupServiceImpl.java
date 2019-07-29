package com.hkt.btu.noc.core.service.impl;

import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import com.hkt.btu.noc.core.dao.entity.NocConfigParamEntity;
import com.hkt.btu.noc.core.dao.entity.NocUserGroupEntity;
import com.hkt.btu.noc.core.dao.mapper.NocUserGroupMapper;
import com.hkt.btu.noc.core.exception.InsufficientAuthorityException;
import com.hkt.btu.noc.core.exception.InvalidInputException;
import com.hkt.btu.noc.core.service.NocConfigParamService;
import com.hkt.btu.noc.core.service.NocUserGroupService;
import com.hkt.btu.noc.core.service.NocUserService;
import com.hkt.btu.noc.core.service.bean.NocUserBean;
import com.hkt.btu.noc.core.service.bean.NocUserGroupBean;
import com.hkt.btu.noc.core.service.populator.NocUserGroupBeanPopulator;
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

public class NocUserGroupServiceImpl implements NocUserGroupService {


    @Resource
    NocUserGroupMapper nocUserGroupMapper;

    @Resource(name = "userGroupBeanPopulator")
    NocUserGroupBeanPopulator nocUserGroupBeanPopulator;

    @Resource(name = "nocConfigParamService")
    NocConfigParamService nocConfigParamService;
    @Resource(name = "userService")
    NocUserService nocUserService;

    @Override
    public List<NocUserGroupBean> getAllUserGroup() {
        List<NocUserGroupBean> result = new LinkedList<>();

        List<NocUserGroupEntity> nocUserGroupEntityList = nocUserGroupMapper.getAllUserGroup();
        if(CollectionUtils.isEmpty(nocUserGroupEntityList)){
            return result;
        }

        for(NocUserGroupEntity entity : nocUserGroupEntityList){
            NocUserGroupBean bean = new NocUserGroupBean();
            nocUserGroupBeanPopulator.populate(entity, bean);
            result.add(bean);
        }

        return result;
    }

    @Override
    public List<NocUserGroupBean> getEligibleUserGroupGrantList() {
        // get current user group
        BtuUser currentUser = nocUserService.getCurrentUser();
        Collection<GrantedAuthority> authorities = currentUser==null ? null : currentUser.getAuthorities();
        if(CollectionUtils.isEmpty(authorities)){
            return null;
        }

        // get all user group
        List<NocUserGroupBean> allUserGroup = getAllUserGroup();
        if(CollectionUtils.isEmpty(allUserGroup)){
            return null;
        }

        // extract eligible groups of current user group
        List<String> eligibleUserGroupIdList = new LinkedList<>();
        for(GrantedAuthority grantedAuthority : authorities){
            if(grantedAuthority instanceof SimpleGrantedAuthority){
                String userGroupId = grantedAuthority.getAuthority();
                String mappedGroupIds = nocConfigParamService.getString(NocConfigParamEntity.USER_GROUP_CREATE_MAPPING.CONFIG_GROUP, userGroupId);
                if(StringUtils.isEmpty(mappedGroupIds)){
                    continue;
                }

                String [] splitGroupIds = mappedGroupIds.split(",");
                List<String> splitGroupIdList = Arrays.asList(splitGroupIds);
                eligibleUserGroupIdList.addAll(splitGroupIdList);
            }
        }

        // add eligible bean to result
        List<NocUserGroupBean> result = new LinkedList<>();
        for(NocUserGroupBean nocUserGroupBean : allUserGroup){
            String userGroupId = nocUserGroupBean.getGroupId();
            if(eligibleUserGroupIdList.contains(userGroupId)){
                result.add(nocUserGroupBean);
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
        List<NocUserGroupBean> eligibleUserGroupList = getEligibleUserGroupGrantList();
        if(CollectionUtils.isEmpty(eligibleUserGroupList)){
            return false;
        }

        List<String> eligibleUserGroupIdList = new LinkedList<>();
        for(NocUserGroupBean nocUserGroupBean :  eligibleUserGroupList){
            eligibleUserGroupIdList.add(nocUserGroupBean.getGroupId());
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
                                Boolean updateIsCAdmin, Boolean updateIsCUser, NocUserBean modifier)
            throws InsufficientAuthorityException, InvalidInputException {
        if(userId==null){
            throw new InvalidInputException("Target user not found.");
        }

        // check user has auth to update
        if(updateIsAdmin!=null || updateIsUser!=null){
            if(! nocUserService.hasAnyAuthority(NocUserGroupEntity.GROUP_ID.ADMIN)){
                throw new InsufficientAuthorityException("Insufficient authority to modify internal user.");
            }
        }
        if(updateIsCAdmin!=null || updateIsCUser!=null){
            if(! nocUserService.hasAnyAuthority(NocUserGroupEntity.GROUP_ID.C_ADMIN, NocUserGroupEntity.GROUP_ID.ADMIN)){
                throw new InsufficientAuthorityException("Insufficient authority to modify external user.");
            }
        }

        Integer modifierUserId = modifier.getUserId();

        if(updateIsAdmin!=null){
            nocUserGroupMapper.deleteUserUserGroup(userId, NocUserGroupEntity.GROUP_ID.ADMIN);
            if(BooleanUtils.isTrue(updateIsAdmin)){
                nocUserGroupMapper.insertUserUserGroup(userId, NocUserGroupEntity.GROUP_ID.ADMIN, modifierUserId);
            }
        }

        if(updateIsUser!=null){
            nocUserGroupMapper.deleteUserUserGroup(userId, NocUserGroupEntity.GROUP_ID.USER);
            if(BooleanUtils.isTrue(updateIsUser)){
                nocUserGroupMapper.insertUserUserGroup(userId, NocUserGroupEntity.GROUP_ID.USER, modifierUserId);
            }
        }

        if(updateIsCAdmin!=null){
            nocUserGroupMapper.deleteUserUserGroup(userId, NocUserGroupEntity.GROUP_ID.C_ADMIN);
            if(BooleanUtils.isTrue(updateIsCAdmin)){
                nocUserGroupMapper.insertUserUserGroup(userId, NocUserGroupEntity.GROUP_ID.C_ADMIN, modifierUserId);
            }
        }

        if(updateIsCUser!=null){
            nocUserGroupMapper.deleteUserUserGroup(userId, NocUserGroupEntity.GROUP_ID.C_USER);
            if(BooleanUtils.isTrue(updateIsCUser)){
                nocUserGroupMapper.insertUserUserGroup(userId, NocUserGroupEntity.GROUP_ID.C_USER, modifierUserId);
            }
        }

    }


}
