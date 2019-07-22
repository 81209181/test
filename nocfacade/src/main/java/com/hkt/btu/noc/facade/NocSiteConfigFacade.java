package com.hkt.btu.noc.facade;


import com.hkt.btu.noc.facade.data.NocSiteConfigData;

public interface NocSiteConfigFacade {
    void reload();

    NocSiteConfigData getSiteInstance();
}
