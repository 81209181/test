package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.service.BtuSiteConfigService;
import com.hkt.btu.sd.core.service.bean.SdSiteConfigBean;

public interface SdSiteConfigService extends BtuSiteConfigService {

    void reload();
    SdSiteConfigBean getSdSiteConfigBean();

    boolean isProductionServer();
    boolean isDevelopmentServer();
}
