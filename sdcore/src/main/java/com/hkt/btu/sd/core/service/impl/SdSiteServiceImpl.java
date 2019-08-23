package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.service.SdSiteService;


public class SdSiteServiceImpl implements SdSiteService {
    @Override
    public boolean isProductionServer() {
        return false;
    }

    @Override
    public boolean isDevelopmentServer() {
        return false;
    }
}
