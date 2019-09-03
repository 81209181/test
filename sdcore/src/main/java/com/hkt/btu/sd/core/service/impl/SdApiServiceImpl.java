package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.BtuSensitiveDataService;
import com.hkt.btu.sd.core.service.SdApiService;
import com.hkt.btu.sd.core.service.SdConfigParamService;
import com.hkt.btu.sd.core.service.SdSiteConfigService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

public class SdApiServiceImpl implements SdApiService {
    private static final Logger LOG = LogManager.getLogger(SdApiServiceImpl.class);

    @Resource(name = "sensitiveDataService")
    BtuSensitiveDataService sensitiveDataService;
    @Resource
    private SdConfigParamService sdConfigParamService;
    @Resource(name = "siteConfigService")
    private SdSiteConfigService siteConfigService;

    // cached api info
    private Map<String, SiteInterfaceBean> cachedApiSiteInterfaceBean = new HashMap<>();

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
        siteConfigService.reload();
    }

    @Override
    public synchronized void reloadCached(String apiName) {
        // reload all cached for input systemName
        Map<String, Object> configParamByConfigGroup = sdConfigParamService.getConfigParamByConfigGroup(apiName, true);

        // populate bean
        SiteInterfaceBean bean = new SiteInterfaceBean();

        if (configParamByConfigGroup == null || configParamByConfigGroup.size() == 0) {
            LOG.warn("Cannot find config param.");
        }

        bean.setSystemName((String) configParamByConfigGroup.get(SiteInterfaceBean.API_CONFIG_KEY.API_CONFIG_KEY_SYSTEM_NAME));
        bean.setUrl((String) configParamByConfigGroup.get(SiteInterfaceBean.API_CONFIG_KEY.API_CONFIG_KEY_URL));
        bean.setUserName((String) configParamByConfigGroup.get(SiteInterfaceBean.API_CONFIG_KEY.API_CONFIG_KEY_USER_NAME));
        bean.setPassword((String) configParamByConfigGroup.get(SiteInterfaceBean.API_CONFIG_KEY.API_CONFIG_KEY_PASSWORD));
        bean.setxAppkey((String) configParamByConfigGroup.get(SiteInterfaceBean.API_CONFIG_KEY.API_CONFIG_KEY_X_APPKEY));
        bean.setBeId((String) configParamByConfigGroup.get(SiteInterfaceBean.API_CONFIG_KEY.API_CONFIG_KEY_BE_ID));
        bean.setChannelType((String) configParamByConfigGroup.get(SiteInterfaceBean.API_CONFIG_KEY.API_CONFIG_KEY_CHANNEL_TYPE));

        // cache bean
        LOG.info("Reloaded cached api bean: " + apiName);
        cachedApiSiteInterfaceBean.put(apiName, bean);
    }
}
