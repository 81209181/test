package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.service.BtuApiClientService;
import com.hkt.btu.common.core.service.BtuCacheService;
import com.hkt.btu.common.core.service.BtuConfigParamService;
import com.hkt.btu.common.core.service.constant.BtuCacheEnum;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

public class BtuApiClientServiceImpl implements BtuApiClientService {
    private static final Logger LOG = LogManager.getLogger(BtuApiClientServiceImpl.class);

    @Resource(name = "configParamService")
    BtuConfigParamService btuConfigParamService;
    @Resource(name = "cacheService")
    BtuCacheService btuCacheService;


    @Override
    public Map<String, String> loadApiClientBean() {
        Map<String, Object> dbApiKeyMap = btuConfigParamService.getConfigParamByConfigGroup(BtuConfigParamEntity.API_CLIENT.CONFIG_GROUP, true);
        if(MapUtils.isEmpty(dbApiKeyMap)){
            LOG.warn("Cannot find any api key for other system to use.");
            return new HashMap<>();
        }

        LOG.info("Creating new api key map (size={})...", MapUtils.size(dbApiKeyMap));
        Map<String, String> newCacheKeyMap = new HashMap<>();
        dbApiKeyMap.forEach( (k, v)->{
            String [] configKeyArray = StringUtils.split(k, ".");
            if(ArrayUtils.getLength(configKeyArray)<2){
                return;
            }

            String apiName = configKeyArray[0];
            String apiKey = (String) v;
            if (StringUtils.equals(configKeyArray[1], "key")) {
                newCacheKeyMap.put(apiName, apiKey);
            }
        });
        return newCacheKeyMap;
    }

    @Override
    public void reloadCache() {
        btuCacheService.reloadCachedObject(BtuCacheEnum.API_CLIENT_MAP.getCacheName());
    }



    @Override
    public String getApiClientKey(String apiName) {
        // try find in cache
        Map<String, String> apiClientMap = (Map<String, String>) btuCacheService.getCachedObjectByCacheName(BtuCacheEnum.API_CLIENT_MAP.getCacheName());
        return MapUtils.getString(apiClientMap, apiName);
    }

    @Override
    public boolean checkApiClientKey(String apiName, String inputKey) {
        String targetKey = getApiClientKey(apiName);
        return StringUtils.equals(inputKey, targetKey);
    }


}
