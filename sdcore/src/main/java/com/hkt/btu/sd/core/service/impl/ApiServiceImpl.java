package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.BtuSensitiveDataService;
import com.hkt.btu.sd.core.service.ApiService;
import com.hkt.btu.sd.core.service.SdConfigParamService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

public class ApiServiceImpl implements ApiService {
    private static final Logger LOG = LogManager.getLogger(ApiServiceImpl.class);

    @Resource(name = "sensitiveDataService")
    BtuSensitiveDataService sensitiveDataService;
    @Resource
    private SdConfigParamService sdConfigParamService;

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

        // populate bean
        SiteInterfaceBean bean = new SiteInterfaceBean();

        for(String key : configParamByConfigGroup.keySet()){
            String value = (String) configParamByConfigGroup.get(key);

            if (key.equalsIgnoreCase("systemName")) {
                bean.setSystemName(value);
            }else if (key.equalsIgnoreCase("url")){
                bean.setUrl(value);
            } else if (key.equalsIgnoreCase("userName")) {
                bean.setUserName(value);
            } else if (key.equalsIgnoreCase("password")) {
                bean.setPassword(value);
            } else if (key.equalsIgnoreCase("xAppKey")) {
                bean.setxAppkey(value);
            } else if (key.equalsIgnoreCase("beId")) {
                bean.setBeId(value);
            } else if (key.equalsIgnoreCase("channelType")) {
                bean.setChannelType(value);
            }
        }

        // cache bean
        LOG.info("Reloaded cached api bean: " + apiName);
        cachedApiSiteInterfaceBean.put(apiName, bean);
    }
}