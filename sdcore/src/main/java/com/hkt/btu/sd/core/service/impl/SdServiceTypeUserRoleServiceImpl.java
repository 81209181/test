package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdServiceTypeUserRoleEntity;
import com.hkt.btu.sd.core.dao.mapper.SdServiceTypeUserRoleMapper;
import com.hkt.btu.sd.core.service.SdServiceTypeUserRoleService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeUserRoleBean;
import com.hkt.btu.sd.core.service.populator.SdServiceTypeUserRoleBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SdServiceTypeUserRoleServiceImpl implements SdServiceTypeUserRoleService {
    private static final Logger LOG = LogManager.getLogger(SdServiceTypeUserRoleServiceImpl.class);

    @Resource
    private SdServiceTypeUserRoleMapper sdServiceTypeUserRoleMapper;

    @Resource(name = "userService")
    SdUserService userService;

    @Resource(name = "serviceTypeUserRoleBeanPopulator")
    SdServiceTypeUserRoleBeanPopulator serviceTypeUserRoleBeanPopulator;

    @Override
    public List<SdServiceTypeUserRoleBean> getServiceTypeUserRoleByServiceType(String serviceType) {
        return sdServiceTypeUserRoleMapper.getServiceTypeUserRole(serviceType).stream().map(entity -> {
            SdServiceTypeUserRoleBean bean = new SdServiceTypeUserRoleBean();
            serviceTypeUserRoleBeanPopulator.populate(entity, bean);
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    public void editServiceTypeUserRole(String serviceType, List<String> userRoleId) {
        String createby = userService.getCurrentUserUserId();

        // get all existing service type list of the user role
        List<SdServiceTypeUserRoleEntity> existServiceTypeUserRoleList = sdServiceTypeUserRoleMapper.getServiceTypeUserRole(serviceType);
        if (CollectionUtils.isEmpty(existServiceTypeUserRoleList)) {
            if (!CollectionUtils.isEmpty(userRoleId)) {
                LOG.info("Created serviceType:" + serviceType + ", userRoleId:" + userRoleId);
                sdServiceTypeUserRoleMapper.createServiceTypeUserRole(userRoleId, serviceType, createby);
            }
        } else {
            List<String> existRoleIdList = new ArrayList<>();
            for (SdServiceTypeUserRoleEntity serviceTypeUserRoleEntity : existServiceTypeUserRoleList) {
                existRoleIdList.add(serviceTypeUserRoleEntity.getRoleId());
            }

            // find which service type to delete
            List<String> toDeleteRoleIdList = new ArrayList<>(existRoleIdList);
            toDeleteRoleIdList.removeAll(userRoleId);
            LOG.info("Deleted serviceType:" + serviceType + ", toDeleteRoleIdList:" + toDeleteRoleIdList);

            // find which service type to insert
            List<String> toInsertRoleIdList = new ArrayList<>(userRoleId);
            toInsertRoleIdList.removeAll(existRoleIdList);
            LOG.info("Created serviceType:" + serviceType + ", toInsertRoleIdList:" + toInsertRoleIdList);

            // delete role ID
            if (!CollectionUtils.isEmpty(toDeleteRoleIdList)) {
                sdServiceTypeUserRoleMapper.deleteServiceTypeUserRole(serviceType, toDeleteRoleIdList);
            }

            // insert role ID
            if (!CollectionUtils.isEmpty(toInsertRoleIdList)) {
                sdServiceTypeUserRoleMapper.createServiceTypeUserRole(toInsertRoleIdList, serviceType, createby);
            }
        }
    }
}
