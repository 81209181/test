package com.hkt.btu.common.facade.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkt.btu.common.core.service.BtuCacheService;
import com.hkt.btu.common.core.service.bean.BtuCacheBean;
import com.hkt.btu.common.facade.BtuCacheFacade;
import com.hkt.btu.common.facade.data.BtuCacheProfileData;
import com.hkt.btu.common.facade.populator.BtuCacheProfileDataPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class BtuCacheFacadeImpl implements BtuCacheFacade {
    private static final Logger LOG = LogManager.getLogger(BtuCacheFacadeImpl.class);

    @Resource(name="cacheService")
    BtuCacheService btuCacheService;

    @Resource(name="cacheProfileDataPopulator")
    BtuCacheProfileDataPopulator btuCacheProfileDataPopulator;

    private final ObjectMapper om = new ObjectMapper();

    private static final String SENSITIVE = "SENSITIVE";

    @Override
    public List<BtuCacheProfileData> getCacheProfileDataList() {
        List<BtuCacheBean> cacheBeanList = btuCacheService.getAllCachedBean();
        if(CollectionUtils.isEmpty(cacheBeanList)){
            return new ArrayList<>();
        }

        List<BtuCacheProfileData> cacheProfileDataList = new ArrayList<>();
        for(BtuCacheBean btuCacheBean : cacheBeanList){
            BtuCacheProfileData btuCacheProfileData = new BtuCacheProfileData();
            btuCacheProfileDataPopulator.populate(btuCacheBean, btuCacheProfileData);
            cacheProfileDataList.add(btuCacheProfileData);
        }
        return cacheProfileDataList;
    }

    @Override
    public String getCachedObjectJson(String cacheName) {
        try {
            BtuCacheBean profileBean = btuCacheService.getNewCacheProfileBeanByCacheName(cacheName);
            if (profileBean.isSensitive()) {
                return SENSITIVE;
            }
            return om.writeValueAsString(btuCacheService.getCachedObjectByCacheName(cacheName));
        } catch (JsonProcessingException e) {
            LOG.warn(e.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public String getSourceObjectJson(String cacheName) {
        try {
            BtuCacheBean profileBean = btuCacheService.getNewCacheProfileBeanByCacheName(cacheName);
            if (profileBean.isSensitive()) {
                return SENSITIVE;
            }
            return om.writeValueAsString(btuCacheService.getSourceObjectByCacheName(cacheName));
        } catch (JsonProcessingException e) {
            LOG.warn(e.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public String reloadCacheByCacheName(String cacheName) {
        try {
            btuCacheService.reloadCachedObject(cacheName);
            return null;
        }catch (Exception e){
            String errorMsg = String.format("Cannot reload cache: %s", cacheName);
            LOG.error(errorMsg);
            LOG.error(e.getMessage(), e);
            return errorMsg;
        }
    }

    @Override
    public BtuCacheProfileData getCacheProfileDataByCacheName(String cacheName) {
        BtuCacheProfileData cacheProfileData = new BtuCacheProfileData();
        BtuCacheBean btuCacheBean = btuCacheService.getNewCacheProfileBeanByCacheName(cacheName);
        btuCacheProfileDataPopulator.populate(btuCacheBean, cacheProfileData);
        return cacheProfileData;
    }
}