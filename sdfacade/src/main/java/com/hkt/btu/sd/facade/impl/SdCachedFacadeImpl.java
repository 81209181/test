package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.SdServiceTypeService;
import com.hkt.btu.sd.core.service.SdSiteConfigService;
import com.hkt.btu.sd.core.service.SdUserRoleService;
import com.hkt.btu.sd.facade.SdCachedFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;

public class SdCachedFacadeImpl implements SdCachedFacade {

    private static final Logger LOG = LogManager.getLogger(SdCachedFacadeImpl.class);

    @Resource(name = "siteConfigService")
    SdSiteConfigService siteConfigService;
    @Resource(name = "userRoleService")
    SdUserRoleService userRoleService;
    @Resource(name = "serviceTypeService")
    SdServiceTypeService serviceTypeService;

    @Override
    public boolean reloadSiteConfigBean() {
        try {
            siteConfigService.reload();
            return true;
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean reloadServiceTypeOfferMapping() {
        try {
            serviceTypeService.reloadServiceTypeOfferMapping();
            return true;
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean reloadServiceTypeList() {
        try {
            serviceTypeService.reloadServiceTypeList();
            return true;
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean reloadCachedRoleTree() {
        try {
            userRoleService.reloadCachedRoleTree();
            return true;
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return false;
        }
    }

}
