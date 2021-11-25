package com.hkt.btu.common.core.service.populator;


import com.hkt.btu.common.core.service.bean.BtuCacheBean;
import com.hkt.btu.common.core.service.constant.BtuCacheEnum;

import java.time.LocalDateTime;

public class BtuCacheBeanPopulator extends AbstractBeanPopulator<BtuCacheBean> {

    public void populate(BtuCacheEnum btuCacheEnum, BtuCacheBean btuCacheBean){
        final LocalDateTime NOW = LocalDateTime.now();
        btuCacheBean.setCreateDate(NOW);
        btuCacheBean.setModifyDate(NOW);

        btuCacheBean.setCacheName(btuCacheEnum.getCacheName());
        btuCacheBean.setLoadingPriority(btuCacheEnum.getLoadingPriority());
        btuCacheBean.setLazyInit(btuCacheEnum.isLazyInit());
        btuCacheBean.setSensitive(btuCacheEnum.isSensitive());
        btuCacheBean.setOriginServiceBeanName(btuCacheEnum.getOriginServiceBeanName());
        btuCacheBean.setOriginServiceMethodName(btuCacheEnum.getOriginServiceMethodName());
    }

    public void populate(Object cacheObject, BtuCacheBean btuCacheBean){
        final LocalDateTime NOW = LocalDateTime.now();
        btuCacheBean.setModifyDate(NOW);
//        btuCacheBean.setModifyby(btuUserBean.getUserId());

        btuCacheBean.setCacheObject(cacheObject);
    }
}