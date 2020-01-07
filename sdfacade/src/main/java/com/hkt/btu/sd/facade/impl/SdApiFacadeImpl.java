package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;
import com.hkt.btu.sd.core.service.SdConfigParamService;
import com.hkt.btu.sd.facade.SdApiFacade;
import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

public class SdApiFacadeImpl implements SdApiFacade {
    private static final Logger LOG = LogManager.getLogger(SdApiFacadeImpl.class);

    // cached api info
    private Map<String, String> cachedApiSiteInterfaceBean = new HashMap<>();// todo [SERVDESK-308]: this should be useless

    @Resource
    private SdConfigParamService sdConfigParamService;

    public String getSiteInterfaceBean(String apiName){// todo [SERVDESK-308]: this should be useless
        // try find in cache
        if(cachedApiSiteInterfaceBean.size() > 0 && cachedApiSiteInterfaceBean.containsKey(apiName)){
            return cachedApiSiteInterfaceBean.get(apiName);
        }

        // reload
        reloadCached(apiName);

        // try find in cache again, else return null
        return cachedApiSiteInterfaceBean.get(apiName);
    }

    @Override
    public synchronized void reloadCached(String apiName) {
        // reload all cached for input systemName
        Map<String, Object> configParamByConfigGroup = sdConfigParamService.getConfigParamByConfigGroup(BtuConfigParamBean.CONFIG_GROUP.API_CLIENT, true);
        if (MapUtils.isEmpty(configParamByConfigGroup)) {
            LOG.warn("Cannot find config param.");
            return;
        }

        // populate bean
        BtuConfigParamBean bean = new BtuConfigParamBean();
        if (apiName.equals("BES")) {
            bean.setConfigValue((String) configParamByConfigGroup.get(BtuConfigParamBean.API_CONFIG_KEY.BES_KEY));
        } else if (apiName.equals("WFM")) {
            bean.setConfigValue((String) configParamByConfigGroup.get(BtuConfigParamBean.API_CONFIG_KEY.WFM_KEY));
        }

        // cache bean
        LOG.info("Reloaded cached api bean: " + apiName);
        cachedApiSiteInterfaceBean.put(apiName, bean.getConfigValue());
    }
}
