package com.hkt.btu.sd.facade.impl;


import com.hkt.btu.common.core.service.BtuSiteConfigService;
import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import com.hkt.btu.sd.facade.SdSiteConfigFacade;
import com.hkt.btu.sd.facade.data.SdSiteConfigData;
import com.hkt.btu.sd.facade.populator.SdSiteConfigDataPopulator;

import javax.annotation.Resource;

public class SdSiteConfigFacadeImpl implements SdSiteConfigFacade {

    @Resource(name = "siteConfigService")
    BtuSiteConfigService siteConfigService;

    @Resource(name = "siteInstanceDataPopulator")
    SdSiteConfigDataPopulator siteConfigDataPopulator;




    @Override
    public void reload() {
        siteConfigService.reload();
    }

    @Override
    public SdSiteConfigData getSiteInstance() {
        BtuSiteConfigBean sdSiteConfigBean = siteConfigService.getSiteConfigBean();

        SdSiteConfigData data = new SdSiteConfigData();
        siteConfigDataPopulator.populate(sdSiteConfigBean, data);
        return data;
    }
}
