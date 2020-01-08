package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.service.BtuCacheService;
import com.hkt.btu.common.core.service.bean.BtuCacheBean;
import com.hkt.btu.common.core.service.constant.BtuCacheEnum;
import com.hkt.btu.common.core.service.populator.BtuCacheBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class BtuCacheServiceImpl implements BtuCacheService {
    private static final Logger LOG = LogManager.getLogger(BtuCacheServiceImpl.class);

    @Autowired
    private ApplicationContext applicationContext;
    @Resource(name = "cacheBeanPopulator")
    BtuCacheBeanPopulator btuCacheBeanPopulator;

    private static final Map<String, BtuCacheBean> CACHE_BEAN_MAP = new HashMap<>(); // key = cacheName

    @Override
    @PostConstruct
    public void initCacheObjectMap() {
        // load cache profiles
        List<BtuCacheBean> cacheInitBeanList = getAllCacheBeanProfile();
        LOG.info("Loaded cache objects profiles.");

        // sort by loading priority
        cacheInitBeanList.sort(Comparator.comparing(BtuCacheBean::getLoadingPriority));
        LOG.info("Sorted cache objects with loading priority.");


        for (BtuCacheBean btuCacheBean : cacheInitBeanList) {
            // cache object profile
            CACHE_BEAN_MAP.put(btuCacheBean.getCacheName(), btuCacheBean);
            if (btuCacheBean.isLazyInit()) {
                continue;
            }

            // cache object
            try {
                Object newCacheObject = buildCacheObject(btuCacheBean);
                btuCacheBeanPopulator.populate(newCacheObject, btuCacheBean);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }

        LOG.info("Completed loading cache objects.");
    }

    @Override
    public List<BtuCacheBean> getAllCacheBeanProfile() {
        List<BtuCacheBean> cacheInitBeanList = new ArrayList<>();
        for (BtuCacheEnum btuCacheEnum : BtuCacheEnum.values()) {
            BtuCacheBean btuCacheBean = getNewCacheProfileBeanByCacheName(btuCacheEnum.getCacheName());
            cacheInitBeanList.add(btuCacheBean);
        }
        return cacheInitBeanList;
    }

    @Override
    public BtuCacheBean getNewCacheProfileBeanByCacheName(String cacheName) {
        BtuCacheEnum btuCacheEnum = BtuCacheEnum.getEnum(cacheName);
        if (btuCacheEnum == null) {
            LOG.warn("Cannot find cache profile: " + cacheName);
            return null;
        }

        BtuCacheBean btuCacheBean = new BtuCacheBean();
        btuCacheBeanPopulator.populate(btuCacheEnum, btuCacheBean);
        return btuCacheBean;
    }

    @Override
    public Object getCachedObjectByCacheName(String cacheName) {
        BtuCacheBean existingCacheBean = CACHE_BEAN_MAP.get(cacheName);
        if (existingCacheBean.getCacheObject() == null && existingCacheBean.isLazyInit()) {
            reloadCachedObject(cacheName);
        }
        return existingCacheBean.getCacheObject();
    }

    @Override
    public Object getSourceObjectByCacheName(String cacheName) {
        BtuCacheBean btuCacheBean = getNewCacheProfileBeanByCacheName(cacheName);
        return buildCacheObject(btuCacheBean);
    }

    @Override
    public Object buildCacheObject(BtuCacheBean btuCacheBean) {
        try {
            Object originServiceBean = applicationContext.getBean(btuCacheBean.getOriginServiceBeanName());
            Method originServiceMethod = originServiceBean.getClass().getMethod(btuCacheBean.getOriginServiceMethodName());
            Object cacheObject = originServiceMethod.invoke(originServiceBean);
            LOG.info("Built to-be-cached object: " + btuCacheBean.getCacheName());
            return cacheObject;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            String errorMsg = String.format("Cannot build object for cache. (cacheName: %s)", btuCacheBean.getCacheName());
            LOG.error(errorMsg);
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void reloadAll() {
        List<BtuCacheBean> cacheBeanList = getAllCachedBean();
        if (CollectionUtils.isEmpty(cacheBeanList)) {
            LOG.warn("No cache to reload.");
            return;
        }

        for (BtuCacheBean cacheBean : cacheBeanList) {
            reloadCachedObject(cacheBean.getCacheName());
        }
    }

    @Override
    public synchronized void reloadCachedObject(String cacheName) {
        // get new content to cache
        BtuCacheBean existingCacheBean = CACHE_BEAN_MAP.get(cacheName);
        Object newCacheObject = buildCacheObject(existingCacheBean);
        if (newCacheObject == null) {
            LOG.warn("Cannot cache null:" + cacheName);
            return;
        }

        // update cache
        btuCacheBeanPopulator.populate(newCacheObject, existingCacheBean);
        LOG.info("Reloaded new cache object: " + cacheName);

        // log size
        if (newCacheObject instanceof Collection) {
            LOG.info("New cache collection size: " + CollectionUtils.size(newCacheObject));
        } else if (newCacheObject instanceof Map) {
            LOG.info("New cache map size: " + MapUtils.size((Map<?, ?>) newCacheObject));
        }
    }

    @Override
    public void deleteCachedObject(String cacheName) {
        BtuCacheBean existingCacheBean = CACHE_BEAN_MAP.get(cacheName);
        if (existingCacheBean == null) {
            LOG.warn("Cannot find and delete cached object: " + cacheName);
        } else {
            CACHE_BEAN_MAP.put(cacheName, null);
            LOG.info("Deleted cached object: " + cacheName);
        }
    }

    @Override
    public List<BtuCacheBean> getAllCachedBean() {
        return new ArrayList<>(CACHE_BEAN_MAP.values());
    }


}
