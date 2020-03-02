package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.service.BtuApiService;
import com.hkt.btu.common.core.service.BtuCacheService;
import com.hkt.btu.common.core.service.BtuConfigParamService;
import com.hkt.btu.common.core.service.bean.BtuApiProfileBean;
import com.hkt.btu.common.core.service.populator.BtuApiProfileBeanPopulator;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Base64;
import java.util.Map;

public class BtuApiServiceImpl implements BtuApiService {
    private static final Logger LOG = LogManager.getLogger(BtuApiServiceImpl.class);

    @Resource(name = "configParamService")
    private BtuConfigParamService configParamService;
    @Resource(name = "cacheService")
    private BtuCacheService cacheService;

    @Resource(name = "apiProfileBeanPopulator")
    BtuApiProfileBeanPopulator apiProfileBeanPopulator;

    protected BtuApiProfileBean getNewProfileBeanInternal(){
        return new BtuApiProfileBean();
    }

    @Override
    public BtuApiProfileBean getApiProfileBeanInternal(String apiName){
        return (BtuApiProfileBean) cacheService.getCachedObjectByCacheName(apiName);
    }

    @Override
    public BtuApiProfileBean loadApiProfileBeanInternal(String apiName) {
        LOG.info("Getting config param for API Profile Bean... (apiName={})", apiName);

        // get api profile from config param
        Map<String, Object> configParamGroupMap = configParamService.getConfigParamByConfigGroup(apiName, true);
        if (MapUtils.isEmpty(configParamGroupMap)) {
            LOG.warn("Cannot find config param. (configGroup={})", apiName);
            return null;
        }

        // categorize into header/queryParam/other map
        MultivaluedMap<String, Object> headerMap = new MultivaluedHashMap<>();
        MultivaluedMap<String, Object> queryParamMap = new MultivaluedHashMap<>();
        MultivaluedMap<String, Object> otherParamMap = new MultivaluedHashMap<>();
        for(String key : configParamGroupMap.keySet()){
            Object value = configParamGroupMap.get(key);
            if( StringUtils.startsWith(key, BtuConfigParamEntity.API.CONFIG_KEY.PREFIX_HEADER) ){
                String headerKey = StringUtils.remove(key, BtuConfigParamEntity.API.CONFIG_KEY.PREFIX_HEADER);
                headerMap.putSingle(headerKey, value);
            } else if ( StringUtils.startsWith(key, BtuConfigParamEntity.API.CONFIG_KEY.PREFIX_QUERY_PARAM) ) {
                String queryParamKey = StringUtils.remove(key, BtuConfigParamEntity.API.CONFIG_KEY.PREFIX_QUERY_PARAM);
                queryParamMap.putSingle(queryParamKey, value);
            } else {
                otherParamMap.putSingle(key, value);
            }
        }

        // populate bean
        LOG.info("Loading API profile bean... (apiName={})", apiName);
        BtuApiProfileBean bean = getNewProfileBeanInternal();
        apiProfileBeanPopulator.populate(configParamGroupMap, bean);
        bean.setHeaderMap(headerMap);
        bean.setQueryParamMap(queryParamMap);
        bean.setOtherParamMap(otherParamMap);

        // form btu auth key into header (if applicable)
        String btuHeaderAuthKey = formBasicHeaderAuthKey(bean.getUserName(), bean.getPassword());
        if(StringUtils.isNotEmpty(btuHeaderAuthKey)){
            headerMap.add(HttpHeaders.AUTHORIZATION, btuHeaderAuthKey);
        }

        LOG.info("Loaded API profile bean. (apiName={}, type={})", apiName, bean.getClass());
        return bean;
    }

    // https://en.wikipedia.org/wiki/Basic_access_authentication
    private String formBasicHeaderAuthKey(String userName, String password) {
        if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)){
            return null;
        }

        String authPlainText = String.format("%s:%s", userName, password);
        String encodedAuth = Base64.getEncoder().encodeToString(authPlainText.getBytes());
        return String.format("Basic %s", encodedAuth);
    }
}
