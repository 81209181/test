package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.service.BtuCacheService;
import com.hkt.btu.common.core.service.bean.BtuCacheInfoBean;
import com.hkt.btu.common.core.service.constant.BtuCacheEnum;
import com.hkt.btu.sd.facade.SdCachedFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.List;

public class SdCachedFacadeImpl implements SdCachedFacade {

    private static final Logger LOG = LogManager.getLogger(SdCachedFacadeImpl.class);

    @Resource(name = "cacheService")
    BtuCacheService cacheService;

    @Override
    public void reloadCache(String cacheId) throws Exception {
        cacheService.reloadCache(cacheId);
    }

    @Override
    public String getCacheInfo(String cacheId) throws Exception {
        return cacheService.getCache(cacheId);
    }

    @Override
    public List<BtuCacheInfoBean> getCacheInfoList() {
        return BtuCacheEnum.getCacheInfoList();
    }
}
