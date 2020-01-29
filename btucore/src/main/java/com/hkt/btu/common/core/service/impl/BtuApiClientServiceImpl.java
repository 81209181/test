package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.service.BtuApiClientService;
import com.hkt.btu.common.core.service.BtuCacheService;
import com.hkt.btu.common.core.service.BtuConfigParamService;
import com.hkt.btu.common.core.service.BtuSensitiveDataService;
import com.hkt.btu.common.core.service.constant.BtuCacheEnum;
import com.hkt.btu.common.core.service.constant.BtuConfigParamTypeEnum;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Base64Utils;

import javax.annotation.Resource;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BtuApiClientServiceImpl implements BtuApiClientService {
    private static final Logger LOG = LogManager.getLogger(BtuApiClientServiceImpl.class);

    @Resource(name = "configParamService")
    BtuConfigParamService btuConfigParamService;
    @Resource(name = "cacheService")
    BtuCacheService btuCacheService;
    @Resource(name = "sensitiveDataService")
    BtuSensitiveDataService btuSensitiveDataService;


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

    @Override
    public void reloadApiClientKey(String apiName) {
        String dbKey = btuConfigParamService.getString(BtuConfigParamEntity.API_CLIENT.CONFIG_GROUP, String.format("%s.key", apiName));
        String newKey = btuSensitiveDataService.decryptToStringSafe(Base64Utils.decodeFromString(dbKey));

        Map<String, String> cachedObjectMap = (Map<String, String>) btuCacheService.getCachedObjectByCacheName(BtuCacheEnum.API_CLIENT_MAP.getCacheName());
        cachedObjectMap.forEach((k, v)->{
            if (StringUtils.equals(k, apiName)) {
                cachedObjectMap.put(apiName, newKey);
            }
        });
        btuCacheService.reloadCachedObject(BtuCacheEnum.API_CLIENT_MAP.getCacheName(), cachedObjectMap);
    }

    @Override
    public void regenerateApiClientKey(String apiName) {
        UUID uuid = UUID.randomUUID();
        String configKey = String.format("%s.key", apiName);
        String configValue = uuid.toString();

        try{
            btuConfigParamService.updateConfigParam(BtuConfigParamEntity.API_CLIENT.CONFIG_GROUP, configKey, configValue,
                    BtuConfigParamTypeEnum.STRING, BtuConfigParamEntity.ENCRYPT.YES);
            LOG.info("Re-generated API Client key: {}", apiName);
        } catch (GeneralSecurityException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
