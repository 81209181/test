package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.bean.BtuUserRolePathCtrlBean;
import com.hkt.btu.sd.core.dao.entity.SdUserGroupPathCtrlEntity;
import com.hkt.btu.sd.core.dao.entity.SdUserRoleEntity;
import com.hkt.btu.sd.core.dao.entity.SdUserRolePathCtrlEntity;
import com.hkt.btu.sd.core.dao.mapper.SdUserRoleMapper;
import com.hkt.btu.sd.core.dao.mapper.SdUserRolePathCtrlMapper;
import com.hkt.btu.sd.core.service.SdPathCtrlService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdUserRoleBean;
import com.hkt.btu.sd.core.service.bean.SdUserRolePathCtrlBean;
import com.hkt.btu.sd.core.service.populator.SdUserRoleBeanPopulator;
import com.hkt.btu.sd.core.service.populator.SdUserRolePathCtrlBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SdPathCtrlServiceImpl implements SdPathCtrlService {

    @Resource
    SdUserRolePathCtrlMapper sdUserRolePathCtrlMapper;

    @Resource(name = "pathCtrlBeanPopulator")
    SdUserRolePathCtrlBeanPopulator pathCtrlBeanPopulator;

    @Resource(name = "userService")
    SdUserService userService;
    @Resource
    SdUserRoleMapper sdUserRoleMapper;
    @Resource(name = "userRoleBeanPopulator")
    SdUserRoleBeanPopulator sdUserRoleBeanPopulator;

    public List<BtuUserRolePathCtrlBean> getActiveCtrlBeanList() {

        List<SdUserRolePathCtrlEntity> userRolePathCtrlEntityList =
                sdUserRolePathCtrlMapper.getByStatus(SdUserGroupPathCtrlEntity.STATUS_ACTIVE);

        if (CollectionUtils.isEmpty(userRolePathCtrlEntityList)){
            return null;
        } else {
            List<BtuUserRolePathCtrlBean> result = new LinkedList<>();

            for(SdUserRolePathCtrlEntity pathCtrlEntity : userRolePathCtrlEntityList){
                SdUserRolePathCtrlBean pathCtrlBean = new SdUserRolePathCtrlBean();
                pathCtrlBeanPopulator.populate(pathCtrlEntity, pathCtrlBean);
                result.add(pathCtrlBean);
            }

            return result;
        }
    }

    @Override
    public List<SdUserRolePathCtrlBean> getParentRolePathByRoleId(String roleId) {

        List<SdUserRolePathCtrlEntity> userRolePathCtrlEntityList =
                sdUserRolePathCtrlMapper.getParentRolePathByRoleId(roleId, SdUserGroupPathCtrlEntity.STATUS_ACTIVE);

        if (CollectionUtils.isEmpty(userRolePathCtrlEntityList)){
            return null;
        } else {
            List<SdUserRolePathCtrlBean> result = new LinkedList<>();

            for(SdUserRolePathCtrlEntity pathCtrlEntity : userRolePathCtrlEntityList){
                SdUserRolePathCtrlBean pathCtrlBean = new SdUserRolePathCtrlBean();
                pathCtrlBeanPopulator.populate(pathCtrlEntity, pathCtrlBean);
                result.add(pathCtrlBean);
            }

            return result;
        }
    }

    @Override
    public List<SdUserRoleBean> getAbstractParentRole(String roldId) {
        return sdUserRoleMapper.getParentRoleByRoleId(roldId).stream()
                .filter(entity -> StringUtils.equals(SdUserRoleEntity.IS_ABSTRACT, entity.getAbstractFlag()))
                .map(entity -> {
                    SdUserRoleBean bean = new SdUserRoleBean();
                    sdUserRoleBeanPopulator.populate(entity, bean);
                    return bean;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SdUserRolePathCtrlBean> getActivePathCtrl() {
        return sdUserRolePathCtrlMapper.getActivePathCtrl().stream().map(entity -> {
            SdUserRolePathCtrlBean bean = new SdUserRolePathCtrlBean();
            pathCtrlBeanPopulator.populate(entity, bean);
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    public String createUserRolePathCtrl(String roleId, List<Integer> pathCtrlIdList) {
        String userId = userService.getCurrentUserUserId();
        try {
            sdUserRolePathCtrlMapper.createUserRolePathCtrl(roleId, pathCtrlIdList, userId);
        } catch (DuplicateKeyException e) {
            return "Some data is duplicated.";
        }
        return null;
    }

    @Override
    public String delUserRolePathCtrl(String roleId, int pathCtrlId) {
        Optional<SdUserRolePathCtrlEntity> optionalEntity = Optional.ofNullable(sdUserRolePathCtrlMapper.getPathCtrlById(pathCtrlId));
        if (optionalEntity.isPresent()) {
            sdUserRolePathCtrlMapper.delUserRolePathCtrl(roleId, pathCtrlId);
            return null;
        } else {
            return "path not found.";
        }
    }

}
