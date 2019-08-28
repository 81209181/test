package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.BtuSensitiveDataService;
import com.hkt.btu.sd.core.service.ApiService2;
import com.hkt.btu.sd.core.service.SdConfigParamService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Base64Utils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

public class ApiServiceImpl implements ApiService2 {
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
        reloadCached(SiteInterfaceBean.BES.API_NAME);
        reloadCached(SiteInterfaceBean.ITSM.API_NAME);
        reloadCached(SiteInterfaceBean.ITSM_RESTFUL.API_NAME);
        reloadCached(SiteInterfaceBean.NORARS.API_NAME);
        reloadCached(SiteInterfaceBean.WFM.API_NAME);
    }

    @Override
    public synchronized void reloadCached(String apiName) {
        // reload all cached for input systemName
        sdConfigParamService.getConfigParamByConfigGroup(apiName);

        // populate bean
        SiteInterfaceBean bean = new SiteInterfaceBean();
//        for (SdConfigParamEntity entity : entities) {
//            String value = entity.getConfigValue();
//            if (entity.getEncrypt().equalsIgnoreCase(BtuConfigParamEntity.ENCRYPT.YES)) {
//                value = sensitiveDataService.decryptToStringSafe(Base64Utils.decodeFromString(entity.getConfigValue()));
//            }
//            if (entity.getConfigKey().equalsIgnoreCase("systemName")) {
//                bean.setSystemName(value);
//            }else if (entity.getConfigKey().equalsIgnoreCase("url")){
//                bean.setUrl(value);
//            } else if (entity.getConfigKey().equalsIgnoreCase("userName")) {
//                bean.setUserName(value);
//            } else if (entity.getConfigKey().equalsIgnoreCase("password")) {
//                bean.setPassword(value);
//            } else if (entity.getConfigKey().equalsIgnoreCase("xAppKey")) {
//                bean.setxAppkey(value);
//            } else if (entity.getConfigKey().equalsIgnoreCase("beId")) {
//                bean.setBeId(value);
//            } else if (entity.getConfigKey().equalsIgnoreCase("channelType")) {
//                bean.setChannelType(value);
//            }
//        }

        // cache bean
        LOG.info("Reloaded cached api bean: " + apiName);
        cachedApiSiteInterfaceBean.put(apiName, bean);
    }
}
