package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.bean.BtuCacheBean;
import com.hkt.btu.common.core.service.populator.BtuCacheBeanPopulator;
import com.hkt.btu.sd.core.service.constant.SdCacheEnum;

import java.time.LocalDateTime;


public class SdCacheBeanPopulator extends BtuCacheBeanPopulator {

    public void populate(SdCacheEnum sdCacheEnum, BtuCacheBean btuCacheBean){
        final LocalDateTime NOW = LocalDateTime.now();
        btuCacheBean.setCreatedate(NOW);
        btuCacheBean.setModifydate(NOW);

        btuCacheBean.setCacheName(sdCacheEnum.getCacheName());
        btuCacheBean.setLoadingPriority(sdCacheEnum.getLoadingPriority());
        btuCacheBean.setLazyInit(sdCacheEnum.isLazyInit());
        btuCacheBean.setOriginServiceBeanName(sdCacheEnum.getOriginServiceBeanName());
        btuCacheBean.setOriginServiceMethodName(sdCacheEnum.getOriginServiceMethodName());
    }

}