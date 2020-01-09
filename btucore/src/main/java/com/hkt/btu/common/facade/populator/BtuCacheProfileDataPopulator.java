package com.hkt.btu.common.facade.populator;

import com.hkt.btu.common.core.service.bean.BtuCacheBean;
import com.hkt.btu.common.facade.data.BtuCacheProfileData;

public class BtuCacheProfileDataPopulator extends AbstractDataPopulator<BtuCacheProfileData> {

    public void populate(BtuCacheBean source, BtuCacheProfileData target) {
        target.setCacheName(source.getCacheName());
        target.setOriginServiceBeanName(source.getOriginServiceBeanName());
        target.setOriginServiceMethodName(source.getOriginServiceMethodName());

        target.setLoadingPriority(source.getLoadingPriority());
        target.setLazyInit(source.isLazyInit());
        target.setSensitive(source.isSensitive());

        target.setCreateby(source.getCreateby());
        target.setCreatedate(source.getCreatedate());
        target.setModifyby(source.getModifyby());
        target.setModifydate(source.getModifydate());
    }
}
