package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.BtuSensitiveDataService;
import com.hkt.btu.sd.core.service.SdApiService;
import com.hkt.btu.sd.core.service.SdConfigParamService;
import com.hkt.btu.sd.core.service.SdSiteConfigService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;
import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

public class SdApiServiceImpl implements SdApiService {
    private static final Logger LOG = LogManager.getLogger(SdApiServiceImpl.class);

    // cached api info
    private Map<String, SiteInterfaceBean> cachedApiSiteInterfaceBean = new HashMap<>();

    @Resource
    private SdConfigParamService sdConfigParamService;



    public SiteInterfaceBean getSiteInterfaceBean(String apiName){
        // try find in cache
        SiteInterfaceBean siteInterfaceBean = cachedApiSiteInterfaceBean.get(apiName);
        if(siteInterfaceBean!=null){
            return siteInterfaceBean;
        }

        // reload
        reloadCached(apiName);

        // try find in cache again, else return null
        return cachedApiSiteInterfaceBean.get(apiName);
    }

    @Override
    public void reloadAllCached() {
        reloadCached(SiteInterfaceBean.API_BES.API_NAME);
        reloadCached(SiteInterfaceBean.API_ITSM.API_NAME);
        reloadCached(SiteInterfaceBean.API_ITSM_RESTFUL.API_NAME);
        reloadCached(SiteInterfaceBean.API_NORARS.API_NAME);
        reloadCached(SiteInterfaceBean.API_WFM.API_NAME);
    }

    @Override
    public synchronized void reloadCached(String apiName) {
        // reload all cached for input systemName
        Map<String, Object> configParamByConfigGroup = sdConfigParamService.getConfigParamByConfigGroup(apiName, true);
        if (MapUtils.isEmpty(configParamByConfigGroup)) {
            LOG.warn("Cannot find config param.");
            return;
        }

        // populate bean
        SiteInterfaceBean bean = new SiteInterfaceBean();
        bean.setSystemName((String) configParamByConfigGroup.get(SiteInterfaceBean.API_CONFIG_KEY.SYSTEM_NAME));
        bean.setUrl((String) configParamByConfigGroup.get(SiteInterfaceBean.API_CONFIG_KEY.KEY_URL));
        bean.setUserName((String) configParamByConfigGroup.get(SiteInterfaceBean.API_CONFIG_KEY.USER_NAME));
        bean.setPassword((String) configParamByConfigGroup.get(SiteInterfaceBean.API_CONFIG_KEY.PASSWORD));
        bean.setxAppkey((String) configParamByConfigGroup.get(SiteInterfaceBean.API_CONFIG_KEY.X_APP_KEY));
        bean.setBeId((String) configParamByConfigGroup.get(SiteInterfaceBean.API_CONFIG_KEY.BE_ID));
        bean.setChannelType((String) configParamByConfigGroup.get(SiteInterfaceBean.API_CONFIG_KEY.CHANNEL_TYPE));

        // cache bean
        LOG.info("Reloaded cached api bean: " + apiName);
        cachedApiSiteInterfaceBean.put(apiName, bean);
    }
}
