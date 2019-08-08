package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.impl.BtuSiteConfigServiceImpl;
import com.hkt.btu.sd.core.service.SdSiteConfigService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SdSiteConfigServiceImpl extends BtuSiteConfigServiceImpl implements SdSiteConfigService {
    private static final Logger LOG = LogManager.getLogger(SdSiteConfigServiceImpl.class);

//    @Override
//    public SdSiteConfigBean getSdSiteConfigBean(){
//        BtuSiteConfigBean siteConfigBean = getSiteConfigBean();
//        if (siteConfigBean instanceof SdSiteConfigBean) {
//            return (SdSiteConfigBean) siteConfigBean;
//        }
//        return null;
//    }


}
