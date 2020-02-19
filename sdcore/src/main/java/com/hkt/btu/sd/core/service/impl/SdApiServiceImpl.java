package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.bean.BtuApiProfileBean;
import com.hkt.btu.common.core.service.impl.BtuApiServiceImpl;
import com.hkt.btu.sd.core.service.SdApiService;
import com.hkt.btu.sd.core.service.bean.SdApiProfileBean;
import com.hkt.btu.sd.core.service.constant.SdCacheEnum;

public class SdApiServiceImpl extends BtuApiServiceImpl implements SdApiService {

    @Override
    protected BtuApiProfileBean getNewProfileBeanInternal(){
        return new SdApiProfileBean();
    }


    @Override
    public SdApiProfileBean loadBesApiProfileBean() {
        return (SdApiProfileBean) loadApiProfileBeanInternal(SdCacheEnum.API_BES.getCacheName());
    }
    @Override
    public SdApiProfileBean getBesApiProfileBean() {
        return (SdApiProfileBean) getApiProfileBeanInternal(SdCacheEnum.API_BES.getCacheName());
    }

    @Override
    public SdApiProfileBean loadItsmApiProfileBean() {
        return (SdApiProfileBean) loadApiProfileBeanInternal(SdCacheEnum.API_ITSM.getCacheName());
    }
    @Override
    public SdApiProfileBean getItsmApiProfileBean() {
        return (SdApiProfileBean) getApiProfileBeanInternal(SdCacheEnum.API_ITSM.getCacheName());
    }

    @Override
    public SdApiProfileBean loadItsmRestfulApiProfileBean() {
        return (SdApiProfileBean) loadApiProfileBeanInternal(SdCacheEnum.API_ITSM_RESTFUL.getCacheName());
    }
    @Override
    public SdApiProfileBean getItsmRestfulApiProfileBean() {
        return (SdApiProfileBean) getApiProfileBeanInternal(SdCacheEnum.API_ITSM_RESTFUL.getCacheName());
    }

    @Override
    public SdApiProfileBean loadNorarsApiProfileBean() {
        return (SdApiProfileBean) loadApiProfileBeanInternal(SdCacheEnum.API_NORARS.getCacheName());
    }
    @Override
    public SdApiProfileBean getNorarsApiProfileBean() {
        return (SdApiProfileBean) getApiProfileBeanInternal(SdCacheEnum.API_NORARS.getCacheName());
    }

    @Override
    public SdApiProfileBean loadWfmApiProfileBean() {
        return (SdApiProfileBean) loadApiProfileBeanInternal(SdCacheEnum.API_WFM.getCacheName());
    }
    @Override
    public SdApiProfileBean getWfmApiProfileBean() {
        return (SdApiProfileBean) getApiProfileBeanInternal(SdCacheEnum.API_WFM.getCacheName());
    }

    @Override
    public SdApiProfileBean loadUtApiProfileBean() {
        return (SdApiProfileBean) loadApiProfileBeanInternal(SdCacheEnum.API_UT_CALL.getCacheName());
    }
    @Override
    public SdApiProfileBean getUtApiProfileBean() {
        return (SdApiProfileBean) getApiProfileBeanInternal(SdCacheEnum.API_UT_CALL.getCacheName());
    }

    @Override
    public SdApiProfileBean loadOssApiProfileBean() {
        return (SdApiProfileBean) loadApiProfileBeanInternal(SdCacheEnum.API_OSS.getCacheName());
    }

    @Override
    public SdApiProfileBean getOssApiProfileBean() {
        return (SdApiProfileBean) getApiProfileBeanInternal(SdCacheEnum.API_OSS.getCacheName());
    }
}
