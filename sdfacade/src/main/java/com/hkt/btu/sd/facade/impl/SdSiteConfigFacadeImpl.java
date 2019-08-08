package com.hkt.btu.sd.facade.impl;


import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import com.hkt.btu.sd.core.service.SdSiteConfigService;
import com.hkt.btu.sd.facade.SdSiteConfigFacade;
import com.hkt.btu.sd.facade.data.SdSiteConfigData;
import com.hkt.btu.sd.facade.populator.SdSiteConfigDataPopulator;

import javax.annotation.Resource;


public class SdSiteConfigFacadeImpl implements SdSiteConfigFacade {

    @Resource(name = "siteConfigService")
    SdSiteConfigService sdSiteConfigService;

    @Resource(name = "siteInstanceDataPopulator")
    SdSiteConfigDataPopulator sdSiteInstanceDataPopulator;



    @Override
    public void reload() {
        sdSiteConfigService.reload();
    }

    @Override
    public SdSiteConfigData getSiteInstance() {
        BtuSiteConfigBean sdSiteConfigBean = sdSiteConfigService.getSiteConfigBean();

        SdSiteConfigData data = new SdSiteConfigData();
        sdSiteInstanceDataPopulator.populate(sdSiteConfigBean, data);
        return data;
    }
}
