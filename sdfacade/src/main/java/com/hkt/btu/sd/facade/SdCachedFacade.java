package com.hkt.btu.sd.facade;

import com.hkt.btu.common.core.service.bean.BtuCacheInfoBean;

import java.util.List;

public interface SdCachedFacade {

    List<BtuCacheInfoBean> getCacheInfoList();

    void reloadCache(String cacheId) throws Exception;

    String getCacheInfo(String cacheId) throws Exception;
}
