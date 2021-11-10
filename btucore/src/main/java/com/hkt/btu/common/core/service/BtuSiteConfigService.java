package com.hkt.btu.common.core.service;

import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;

public interface BtuSiteConfigService {
    BtuSiteConfigBean getSiteConfigBean();

    void reload();

    boolean isProductionServer();

    boolean isDevelopmentServer();

    BtuSiteConfigBean loadSiteConfigBean();
}
