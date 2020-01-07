package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.BtuSensitiveDataService;
import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;
import com.hkt.btu.common.core.service.impl.BtuApiClientServiceImpl;
import com.hkt.btu.sd.core.service.SdApiClientService;
import com.hkt.btu.sd.core.service.SdConfigParamService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Base64Utils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

public class SdApiClientServiceImpl extends BtuApiClientServiceImpl implements SdApiClientService {
    private static final Logger LOG = LogManager.getLogger(SdApiClientServiceImpl.class);

    // cached api info
    private Map<String, String> cachedApiBean = new HashMap<>();

    @Resource(name = "configParamService")
    SdConfigParamService sdConfigParamService;
    @Resource(name = "sensitiveDataService")
    BtuSensitiveDataService sensitiveDataService;

    @Override
    public String getApiClientBean(String apiName){
        // try find in cache
        if(cachedApiBean.size() > 0 && cachedApiBean.containsKey(apiName)){
            return cachedApiBean.get(apiName);
        }

        // reload
        reloadCached(apiName);

        // try find in cache again, else return null
        return cachedApiBean.get(apiName);
    }

    @Override
    public synchronized void reloadCached(String apiName) {
        // reload all cached for input systemName
        String apiClient = sdConfigParamService.getString(BtuConfigParamBean.CONFIG_GROUP.API_CLIENT, String.format("%s.key", apiName));
        String decryptApiClient = sensitiveDataService.decryptToStringSafe(Base64Utils.decodeFromString(apiClient));

        // cache bean
        LOG.info("Reloaded cached api bean: " + apiName);
        cachedApiBean.put(apiName, decryptApiClient);
    }
}
