package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.SdUserRoleServiceTypeService;
import com.hkt.btu.sd.core.service.bean.SdUserRoleServiceTypeBean;
import com.hkt.btu.sd.facade.SdUserRoleServiceTypeFacade;
import com.hkt.btu.sd.facade.data.SdUserRoleServiceTypeData;
import com.hkt.btu.sd.facade.populator.SdUserRoleServiceTypeDataPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

public class SdUserRoleServiceTypeFacadeImpl implements SdUserRoleServiceTypeFacade {
    private static final Logger LOG = LogManager.getLogger(SdUserRoleServiceTypeFacadeImpl.class);


    @Resource(name = "userRoleServiceTypeService")
    SdUserRoleServiceTypeService userRoleServiceTypeService;

    @Resource(name = "userRoleServiceTypeDataPopulator")
    SdUserRoleServiceTypeDataPopulator userRoleServiceTypeDataPopulator;


    @Override
    public List<String> getUserRoleServiceType(String roleId) {
        List<SdUserRoleServiceTypeBean> userRoleServiceTypeBean = userRoleServiceTypeService.getUserRoleServiceType(roleId);
        if (CollectionUtils.isEmpty(userRoleServiceTypeBean)) {
            return null;
        }
        return userRoleServiceTypeBean.stream().map(bean -> {
            SdUserRoleServiceTypeData data = new SdUserRoleServiceTypeData();
            userRoleServiceTypeDataPopulator.populate(bean, data);
            return data;
        }).collect(Collectors.toList())
                .stream()
                .map(SdUserRoleServiceTypeData::getServiceTypeCode)
                .collect(Collectors.toList());
    }
}
