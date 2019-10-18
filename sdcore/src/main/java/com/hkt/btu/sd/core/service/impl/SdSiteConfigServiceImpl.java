package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.impl.BtuSiteConfigServiceImpl;
import com.hkt.btu.sd.core.service.SdServiceTypeService;
import com.hkt.btu.sd.core.service.SdSiteConfigService;
import com.hkt.btu.sd.core.service.SdUserRoleService;

import javax.annotation.Resource;

public class SdSiteConfigServiceImpl extends BtuSiteConfigServiceImpl implements SdSiteConfigService {

    @Resource(name = "userRoleService")
    SdUserRoleService userRoleService;
    @Resource(name = "serviceTypeService")
    SdServiceTypeService serviceTypeService;

    @Override
    public void reload() {
        super.reload();
        userRoleService.reloadCachedRoleTree();
        serviceTypeService.reload();
    }
}
