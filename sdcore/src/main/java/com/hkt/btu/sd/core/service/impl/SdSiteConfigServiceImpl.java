package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import com.hkt.btu.common.core.service.impl.BtuSiteConfigServiceImpl;
import com.hkt.btu.sd.core.service.SdSiteConfigService;
import com.hkt.btu.sd.core.service.bean.SdSiteConfigBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SdSiteConfigServiceImpl extends BtuSiteConfigServiceImpl implements SdSiteConfigService {
    private static final Logger LOG = LogManager.getLogger(SdSiteConfigServiceImpl.class);

    @Override
    public SdSiteConfigBean getSdSiteConfigBean(){
        BtuSiteConfigBean siteConfigBean = getSiteConfigBean();
        if (siteConfigBean instanceof SdSiteConfigBean) {
            return (SdSiteConfigBean) siteConfigBean;
        }
        return null;
    }

    @Override
    public boolean isProductionServer() {
        SdSiteConfigBean sdSiteConfigBean = getSdSiteConfigBean();
        return isProductionServer(sdSiteConfigBean);
    }
    private boolean isProductionServer(SdSiteConfigBean sdSiteConfigBean){
        return StringUtils.equals(SdSiteConfigBean.SERVER_TYPE.PROD, sdSiteConfigBean.getServerType()) ||
                StringUtils.equals(SdSiteConfigBean.SERVER_TYPE.PROD_STANDBY, sdSiteConfigBean.getServerType()) ;
    }

    @Override
    public boolean isDevelopmentServer() {
        SdSiteConfigBean sdSiteConfigBean = getSdSiteConfigBean();
        return isDevelopmentServer(sdSiteConfigBean);
    }
    private boolean isDevelopmentServer(SdSiteConfigBean sdSiteConfigBean){
        return StringUtils.equals(SdSiteConfigBean.SERVER_TYPE.DEV, sdSiteConfigBean.getServerType());
    }
}
