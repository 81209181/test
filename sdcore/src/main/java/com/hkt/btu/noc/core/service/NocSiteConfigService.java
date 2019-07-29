package com.hkt.btu.noc.core.service;

import com.hkt.btu.common.core.service.BtuSiteConfigService;
import com.hkt.btu.noc.core.service.bean.NocSiteConfigBean;

public interface NocSiteConfigService extends BtuSiteConfigService {

    void reload();
    NocSiteConfigBean getNocSiteConfigBean();

    boolean isProductionServer();
    boolean isDevelopmentServer();
}
