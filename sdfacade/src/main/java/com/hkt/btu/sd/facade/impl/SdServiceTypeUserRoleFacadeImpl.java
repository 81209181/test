package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.service.BtuConfigParamService;
import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;
import com.hkt.btu.common.core.service.constant.BtuConfigParamTypeEnum;
import com.hkt.btu.sd.core.service.SdServiceTypeUserRoleService;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeUserRoleBean;
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
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SdServiceTypeUserRoleFacadeImpl implements SdServiceTypeUserRoleFacade {
    private static final Logger LOG = LogManager.getLogger(SdServiceTypeUserRoleFacadeImpl.class);


    @Resource(name = "serviceTypeUserRoleService")
    SdServiceTypeUserRoleService serviceTypeUserRoleService;

    @Resource(name = "configParamService")
    BtuConfigParamService btuConfigParamService;

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
        String searchKey = searchKeys.stream().collect(Collectors.joining(","));
        btuConfigParamService.updateConfigParam(BtuConfigParamEntity.SEARCH_KEY_TYPE_MAPPING.CONFIG_GROUP,
                serviceType, searchKey, BtuConfigParamTypeEnum.STRING, null);
    }

    @Override
    public List<ServiceSearchEnum> getAllSearchKey() {
        List<ServiceSearchEnum> dataList = new LinkedList<>();
        ServiceSearchEnum[] searchEnums = ServiceSearchEnum.values();
        for (ServiceSearchEnum searchEnum : searchEnums) {
            dataList.add(searchEnum);
        }
        return dataList;
    }

    @Override
    public List<String> getSearchKeyMapping(String serviceType) {
        List<String> dataList = new LinkedList<>();

        BtuConfigParamBean configParamBean = btuConfigParamService.getConfigParamByGroupAndKey(BtuConfigParamEntity.SEARCH_KEY_TYPE_MAPPING.CONFIG_GROUP, serviceType);
        if (configParamBean == null) {
            return null;
        }

        String configValue = configParamBean.getConfigValue();
        String[] searchKeys = configValue.split(",");
        for (String searchKey : searchKeys) {
            dataList.add(searchKey);
        }

        return dataList;
    }
}
