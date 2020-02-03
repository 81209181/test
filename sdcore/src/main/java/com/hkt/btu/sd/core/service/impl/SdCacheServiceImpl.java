package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.bean.BtuCacheBean;
import com.hkt.btu.common.core.service.impl.BtuCacheServiceImpl;
import com.hkt.btu.sd.core.service.SdCacheService;
import com.hkt.btu.sd.core.service.bean.SdCacheBean;
import com.hkt.btu.sd.core.service.constant.SdCacheEnum;
import com.hkt.btu.sd.core.service.populator.SdCacheBeanPopulator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.List;

public class SdCacheServiceImpl extends BtuCacheServiceImpl implements SdCacheService {
    private static final Logger LOG = LogManager.getLogger(SdCacheServiceImpl.class);

    @Resource(name = "cacheBeanPopulator")
    SdCacheBeanPopulator cacheBeanPopulator;

    @Override
    public List<BtuCacheBean> getAllCacheBeanProfile() {
        List<BtuCacheBean> cacheInitBeanList = super.getAllCacheBeanProfile();

        for (SdCacheEnum sdCacheEnum : SdCacheEnum.values()) {
            BtuCacheBean btuCacheBean = getNewCacheProfileBeanByCacheName(sdCacheEnum.getCacheName());
            cacheInitBeanList.add(btuCacheBean);
        }
        return cacheInitBeanList;
    }

    @Override
    public BtuCacheBean getNewCacheProfileBeanByCacheName(String cacheName) {
        BtuCacheBean btuCacheBean = super.getNewCacheProfileBeanByCacheName(cacheName);
        if (btuCacheBean != null) {
            return btuCacheBean;
        }

        SdCacheEnum sdCacheEnum = SdCacheEnum.getEnum(cacheName);
        if (sdCacheEnum == null) {
            LOG.warn("Cannot find cache profile: " + cacheName);
            return null;
        }

        SdCacheBean sdCacheBean = new SdCacheBean();
        cacheBeanPopulator.populate(sdCacheEnum, sdCacheBean);
        return sdCacheBean;
    }
}
