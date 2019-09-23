package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.impl.BtuSiteConfigServiceImpl;
import com.hkt.btu.sd.core.service.SdSiteConfigService;
import com.hkt.btu.sd.core.service.SdUserRoleService;

import javax.annotation.Resource;

public class SdSiteConfigServiceImpl extends BtuSiteConfigServiceImpl implements SdSiteConfigService {

    @Resource(name = "userRoleService")
    SdUserRoleService userRoleService;

    @Override
    public void reload() {
        super.reload();
        userRoleService.getCachedRoleAssignMap(null);
    }
}
