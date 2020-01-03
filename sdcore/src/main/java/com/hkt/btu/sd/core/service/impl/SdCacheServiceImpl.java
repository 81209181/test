package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.bean.BtuCacheBean;
import com.hkt.btu.common.core.service.impl.BtuCacheServiceImpl;
import com.hkt.btu.sd.core.service.SdCacheService;
import com.hkt.btu.sd.core.service.constant.SdCacheEnum;

import java.util.List;

public class SdCacheServiceImpl extends BtuCacheServiceImpl implements SdCacheService {
    @Override
    public List<BtuCacheBean> getAllCacheBeanProfile() {
        List<BtuCacheBean> cacheInitBeanList = super.getAllCacheBeanProfile();

        // todo: sd enum version
        for(SdCacheEnum sdCacheEnum : SdCacheEnum.values()){
            BtuCacheBean btuCacheBean = getNewCacheProfileBeanByCacheName(sdCacheEnum.getCacheName());
            cacheInitBeanList.add(btuCacheBean);
        }


        return cacheInitBeanList;
    }

    @Override
    public BtuCacheBean getNewCacheProfileBeanByCacheName(String cacheName) {
//        BtuCacheEnum btuCacheEnum = BtuCacheEnum.getEnum(cacheName);
//        if(btuCacheEnum==null){
//            LOG.warn("Cannot find cache profile: " + cacheName);
//            return null;
//        }
//
//        BtuCacheBean btuCacheBean = new BtuCacheBean();
//        btuCacheBeanPopulator.populate(btuCacheEnum, btuCacheBean);
//        return btuCacheBean;

        // todo: sd enum version
        return null;
    }
}
