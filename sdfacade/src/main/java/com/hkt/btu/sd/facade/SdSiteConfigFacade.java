package com.hkt.btu.sd.facade;


import com.hkt.btu.sd.facade.data.SdSiteConfigData;

public interface SdSiteConfigFacade {
    void reload();

    SdSiteConfigData getSiteInstance();
}
