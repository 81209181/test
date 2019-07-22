package com.hkt.btu.noc.facade.impl;


import com.hkt.btu.noc.core.service.NocSiteConfigService;
import com.hkt.btu.noc.core.service.bean.NocSiteConfigBean;
import com.hkt.btu.noc.facade.NocSiteConfigFacade;
import com.hkt.btu.noc.facade.data.NocSiteConfigData;
import com.hkt.btu.noc.facade.populator.NocSiteConfigDataPopulator;

import javax.annotation.Resource;


public class NocSiteConfigFacadeImpl implements NocSiteConfigFacade {

    @Resource(name = "siteConfigService")
    NocSiteConfigService nocSiteConfigService;

    @Resource(name = "siteInstanceDataPopulator")
    NocSiteConfigDataPopulator nocSiteInstanceDataPopulator;



    @Override
    public void reload() {
        nocSiteConfigService.reload();
    }

    @Override
    public NocSiteConfigData getSiteInstance() {
        NocSiteConfigBean nocSiteConfigBean = (NocSiteConfigBean) nocSiteConfigService.getSiteConfigBean();

        NocSiteConfigData data = new NocSiteConfigData();
        nocSiteInstanceDataPopulator.populate(nocSiteConfigBean, data);
        return data;
    }
}
