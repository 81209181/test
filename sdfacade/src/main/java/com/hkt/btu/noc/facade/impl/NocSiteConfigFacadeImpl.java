package com.hkt.btu.noc.facade.impl;


import com.hkt.btu.noc.core.service.NocSiteConfigService;
import com.hkt.btu.noc.core.service.bean.NocSiteConfigBean;
import com.hkt.btu.noc.facade.NocSiteConfigFacade;
import com.hkt.btu.noc.facade.data.NocSiteConfigData;
import com.hkt.btu.noc.facade.populator.NocSiteConfigDataPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class NocSiteConfigFacadeImpl implements NocSiteConfigFacade {

    @Resource(name = "NocSiteConfigService")
    NocSiteConfigService nocSiteConfigService;

    @Autowired
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
