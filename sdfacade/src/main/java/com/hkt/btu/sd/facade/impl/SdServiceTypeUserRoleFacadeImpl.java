package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.SdServiceTypeUserRoleService;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeUserRoleBean;
import com.hkt.btu.sd.facade.SdServiceTypeUserRoleFacade;
import com.hkt.btu.sd.facade.data.SdServiceTypeUserRoleData;
import com.hkt.btu.sd.facade.populator.SdServiceTypeUserRoleDataPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

public class SdServiceTypeUserRoleFacadeImpl implements SdServiceTypeUserRoleFacade {
    private static final Logger LOG = LogManager.getLogger(SdServiceTypeUserRoleFacadeImpl.class);


    @Resource(name = "serviceTypeUserRoleService")
    SdServiceTypeUserRoleService serviceTypeUserRoleService;

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
    public String editServiceTypeUserRole(String serviceType, List<String> userRoleId) {
        if (StringUtils.isEmpty(serviceType)) {
            return "Empty service type.";
        } else if (CollectionUtils.isEmpty(userRoleId)) {
            return "Empty user role.";
        }

        serviceTypeUserRoleService.editServiceTypeUserRole(serviceType, userRoleId);
        return null;
    }
}
