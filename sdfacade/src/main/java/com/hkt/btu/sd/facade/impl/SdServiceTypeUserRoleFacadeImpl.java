package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.service.BtuConfigParamService;
import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;
import com.hkt.btu.common.core.service.constant.BtuConfigParamTypeEnum;
import com.hkt.btu.sd.core.service.SdCacheService;
import com.hkt.btu.sd.core.service.SdServiceTypeUserRoleService;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeUserRoleBean;
import com.hkt.btu.sd.core.service.constant.SdCacheEnum;
import com.hkt.btu.sd.facade.SdServiceTypeUserRoleFacade;
import com.hkt.btu.sd.facade.constant.ServiceSearchEnum;
import com.hkt.btu.sd.facade.data.SdServiceTypeUserRoleData;
import com.hkt.btu.sd.facade.populator.SdServiceTypeUserRoleDataPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SdServiceTypeUserRoleFacadeImpl implements SdServiceTypeUserRoleFacade {
    private static final Logger LOG = LogManager.getLogger(SdServiceTypeUserRoleFacadeImpl.class);


    @Resource(name = "serviceTypeUserRoleService")
    SdServiceTypeUserRoleService serviceTypeUserRoleService;

    @Resource(name = "configParamService")
    BtuConfigParamService btuConfigParamService;

    @Resource(name = "cacheService")
    SdCacheService cacheService;

    @Resource(name = "serviceTypeUserRoleDataPopulator")
    SdServiceTypeUserRoleDataPopulator serviceTypeUserRoleDataPopulator;

    @Override
    public List<String> getServiceTypeUserRoleByServiceType(String serviceType) {
        List<SdServiceTypeUserRoleBean> userRoleServiceTypeBean = serviceTypeUserRoleService.getServiceTypeUserRoleByServiceType(serviceType);
        if (CollectionUtils.isEmpty(userRoleServiceTypeBean)) {
            return null;
        }
        return userRoleServiceTypeBean.stream().map(bean -> {
            SdServiceTypeUserRoleData data = new SdServiceTypeUserRoleData();
            serviceTypeUserRoleDataPopulator.populate(bean, data);
            return data;
        }).collect(Collectors.toList()).stream()
                .map(SdServiceTypeUserRoleData::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    public String editServiceTypeUserRole(String serviceType, List<String> userRoleId, List<String> searchKey) throws GeneralSecurityException {
        if (StringUtils.isEmpty(serviceType)) {
            return "Empty service type.";
        } else if (CollectionUtils.isEmpty(userRoleId)) {
            return "Empty user role.";
        } else if (CollectionUtils.isEmpty(searchKey)) {
            return "Empty search key.";
        }

        serviceTypeUserRoleService.editServiceTypeUserRole(serviceType, userRoleId);
        updateSearchKeyTypeMapping(serviceType, searchKey);
        return null;
    }

    private void updateSearchKeyTypeMapping(String serviceType, List<String> searchKeys) throws GeneralSecurityException {
        String searchKey = String.join(",", searchKeys);
        btuConfigParamService.updateConfigParam(BtuConfigParamEntity.SEARCH_KEY_TYPE_MAPPING.CONFIG_GROUP,
                serviceType, searchKey, BtuConfigParamTypeEnum.STRING, null);
    }

    @Override
    public List<ServiceSearchEnum> getAllSearchKey() {
        ServiceSearchEnum[] searchEnums = ServiceSearchEnum.values();
        return Arrays.asList(searchEnums);
    }

    @Override
    public List<String> getSearchKeyMapping(String serviceType) {
        BtuConfigParamBean configParamBean = btuConfigParamService.getConfigParamByGroupAndKey(
                BtuConfigParamEntity.SEARCH_KEY_TYPE_MAPPING.CONFIG_GROUP, serviceType);
        if (configParamBean == null) {
            return null;
        }

        String configValue = configParamBean.getConfigValue();
        String[] searchKeys = StringUtils.split(configValue, ",");
        return Arrays.asList(searchKeys);
    }

    @Override
    public List<ServiceSearchEnum> getServiceSearchKeyList(List<String> userRole) {
        if (CollectionUtils.isEmpty(userRole)) {
            LOG.warn("Empty userRole.");
            return null;
        }

        // get service search key - service type mapping
        List<BtuConfigParamBean> searchKeyTypeMapping = (List<BtuConfigParamBean>)
                cacheService.getCachedObjectByCacheName(SdCacheEnum.SEARCH_KEY_TYPE_MAPPING.getCacheName());
        if(CollectionUtils.isEmpty(searchKeyTypeMapping)){
            LOG.warn("Empty searchKeyTypeMapping.");
            return null;
        }

        // get service type - user role mapping
        List<SdServiceTypeUserRoleBean> serviceTypeUserRole = (List<SdServiceTypeUserRoleBean>)
                cacheService.getCachedObjectByCacheName(SdCacheEnum.SERVICE_TYPE_USER_ROLE.getCacheName());
        if(CollectionUtils.isEmpty(serviceTypeUserRole)){
            LOG.warn("Empty serviceTypeUserRole.");
            return null;
        }

        // find eligible service type for user
        List<String> filterServiceType = new LinkedList<>();
        serviceTypeUserRole.forEach(sdServiceTypeUserRoleBean -> {
            if (userRole.contains(sdServiceTypeUserRoleBean.getRoleId())) {
                if (!filterServiceType.contains(sdServiceTypeUserRoleBean.getServiceTypeCode())) {
                    filterServiceType.add(sdServiceTypeUserRoleBean.getServiceTypeCode());
                }
            }
        });
        if (CollectionUtils.isEmpty(filterServiceType)) {
            return null;
        }

        // find eligible service search key for user
        List<String> filterSearchKey = new LinkedList<>();
        searchKeyTypeMapping.forEach(btuConfigParamBean -> {
            if (filterServiceType.contains(btuConfigParamBean.getConfigKey())) {
                String[] configValues = btuConfigParamBean.getConfigValue().split(",");
                for (String configValue : configValues) {
                    if (!filterSearchKey.contains(configValue)) {
                        filterSearchKey.add(configValue);
                    }
                }
            }
        });
        if (CollectionUtils.isEmpty(filterSearchKey)) {
            return null;
        }

        // return sorted result
        List<ServiceSearchEnum> serviceSearchKeyList = new LinkedList<>();
        filterSearchKey.forEach(searchKey -> {
            ServiceSearchEnum searchEnum = ServiceSearchEnum.getEnum(searchKey);
            if (searchEnum != null) {
                serviceSearchKeyList.add(searchEnum);
            }
        });
        serviceSearchKeyList.sort(Comparator.comparing(ServiceSearchEnum::getKeyDesc));
        return serviceSearchKeyList;
    }
}
