package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.BtuCacheService;
import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;
import com.hkt.btu.common.core.service.constant.BtuCacheEnum;
import com.hkt.btu.common.core.service.impl.BtuApiClientServiceImpl;
import com.hkt.btu.sd.core.service.SdApiClientService;
import com.hkt.btu.sd.core.service.SdConfigParamService;
import org.apache.commons.collections4.MapUtils;

import javax.annotation.Resource;
import java.util.Map;

public class SdApiClientServiceImpl extends BtuApiClientServiceImpl implements SdApiClientService {

    @Resource(name = "configParamService")
    SdConfigParamService sdConfigParamService;
    @Resource(name = "cacheService")
    BtuCacheService cacheService;

    @Override
    public Map<String, Object> loadApiClientBean() {
        Map<String, Object> configParamByConfigGroup = sdConfigParamService.getConfigParamByConfigGroup(BtuConfigParamBean.CONFIG_GROUP.API_CLIENT, true);
        if (MapUtils.isEmpty(configParamByConfigGroup)) {
            return null;
        }
        return configParamByConfigGroup;
    }

    @Override
    public String getApiClientBean(String apiName) {
        // try find in cache
        Map<String, Object> apiClientMap = (Map<String, Object>) cacheService.getCachedObjectByCacheName(BtuCacheEnum.API_CLIENT_MAP.getCacheName());
        String apiClient = String.format("%s.key", apiName);

        if (MapUtils.isNotEmpty(apiClientMap) && apiClientMap.containsKey(apiClient)) {
            return (String) apiClientMap.get(apiClient);
        }

        return null;
    }

    @Override
    public void reloadCached() {
        cacheService.reloadCachedObject(BtuCacheEnum.API_CLIENT_MAP.getCacheName());
    }
}
