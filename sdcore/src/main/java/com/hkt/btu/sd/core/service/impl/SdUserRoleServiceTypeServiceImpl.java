package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdServiceTypeEntity;
import com.hkt.btu.sd.core.dao.entity.SdUserRoleServiceTypeEntity;
import com.hkt.btu.sd.core.dao.mapper.SdUserRoleServiceTypeMapper;
import com.hkt.btu.sd.core.service.SdUserRoleServiceTypeService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdUserRoleServiceTypeBean;
import com.hkt.btu.sd.core.service.populator.SdUserRoleServiceTypeBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SdUserRoleServiceTypeServiceImpl implements SdUserRoleServiceTypeService {
    private static final Logger LOG = LogManager.getLogger(SdUserRoleServiceTypeServiceImpl.class);

    @Resource
    private SdUserRoleServiceTypeMapper sdUserRoleServiceTypeMapper;

    @Resource(name = "userService")
    SdUserService userService;

    @Resource(name = "userRoleServiceTypeBeanPopulator")
    SdUserRoleServiceTypeBeanPopulator userRoleServiceTypeBeanPopulator;

    @Override
    public List<SdUserRoleServiceTypeBean> getUserRoleServiceType(String roleId) {
        return sdUserRoleServiceTypeMapper.getUserRoleServiceType(roleId).stream().map(entity -> {
            SdUserRoleServiceTypeBean bean = new SdUserRoleServiceTypeBean();
            userRoleServiceTypeBeanPopulator.populate(entity, bean);
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    public void editUserRoleServiceType(String roleId, List<String> serviceTypeList) {
        String createby = userService.getCurrentUserUserId();

        // filter unknown service type
        List<String> filteredServiceTypeList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(serviceTypeList)){
            for (String serviceType : serviceTypeList) {
                if (!StringUtils.equals(SdServiceTypeEntity.SERVICE_TYPE.UNKNOWN, serviceType)) {
                    filteredServiceTypeList.add(serviceType);
                }
            }
        }

        // get all existing service type list of the user role
        List<SdUserRoleServiceTypeEntity> existUserRoleServiceTypeList = sdUserRoleServiceTypeMapper.getUserRoleServiceType(roleId);
        if (CollectionUtils.isEmpty(existUserRoleServiceTypeList)) {
            if (!CollectionUtils.isEmpty(filteredServiceTypeList)) {
                LOG.info("Created roleId:" + roleId + ", serviceTypeList:" + filteredServiceTypeList);
                sdUserRoleServiceTypeMapper.createUserRoleServiceType(filteredServiceTypeList, roleId, createby);
            }
        } else {
            List<String> existServiceTypeList = new ArrayList<>();
            for (SdUserRoleServiceTypeEntity userRoleServiceTypeEntity : existUserRoleServiceTypeList) {
                existServiceTypeList.add(userRoleServiceTypeEntity.getServiceTypeCode());
            }

            // find which service type to delete
            List<String> toDeleteServiceTypeList = new ArrayList<>(existServiceTypeList);
            toDeleteServiceTypeList.removeAll(filteredServiceTypeList);
            LOG.info("Deleted roleId:" + roleId + ", toDeleteServiceTypeList:" + toDeleteServiceTypeList);

            // find which service type to insert
            List<String> toInsertServiceTypeList = new ArrayList<>(filteredServiceTypeList);
            toInsertServiceTypeList.removeAll(existServiceTypeList);
            LOG.info("Created roleId:" + roleId + ", toInsertServiceTypeList:" + toInsertServiceTypeList);

            // delete service type
            if (!CollectionUtils.isEmpty(toDeleteServiceTypeList)) {
                sdUserRoleServiceTypeMapper.deleteUserRoleServiceType(roleId, toDeleteServiceTypeList);
            }

            // insert service type
            if (!CollectionUtils.isEmpty(toInsertServiceTypeList)) {
                sdUserRoleServiceTypeMapper.createUserRoleServiceType(toInsertServiceTypeList, roleId, createby);
            }
        }
    }
}
