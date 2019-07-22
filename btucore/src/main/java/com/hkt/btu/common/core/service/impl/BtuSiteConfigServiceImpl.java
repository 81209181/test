package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.service.BtuSiteConfigService;
import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Site Config Service Implementation for usage in Spring Security
 *
 * @deprecated This should be overridden and implemented by each application.
 * The following implementation is for demo or reference purpose only.
 */
@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated
public class BtuSiteConfigServiceImpl implements BtuSiteConfigService {
    private static final Logger LOG = LogManager.getLogger(BtuSiteConfigService.class);

    @Deprecated
    public BtuSiteConfigBean getSiteConfigBean() {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        return new BtuSiteConfigBean();
    }
}
