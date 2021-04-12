package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.service.BtuApiService;
import com.hkt.btu.sd.core.service.bean.SdApiProfileBean;


public interface SdApiService extends BtuApiService {
    @SuppressWarnings("unused") // used in SdCacheEnum
    SdApiProfileBean loadBesApiProfileBean();
    SdApiProfileBean getBesApiProfileBean();

    @SuppressWarnings("unused") // used in SdCacheEnum
    SdApiProfileBean loadItsmApiProfileBean();
    SdApiProfileBean getItsmApiProfileBean();

    @SuppressWarnings("unused") // used in SdCacheEnum
    SdApiProfileBean loadItsmRestfulApiProfileBean();
    SdApiProfileBean getItsmRestfulApiProfileBean();

    @SuppressWarnings("unused") // used in SdCacheEnum
    SdApiProfileBean loadNorarsApiProfileBean();
    SdApiProfileBean getNorarsApiProfileBean();

    @SuppressWarnings("unused") // used in SdCacheEnum
    SdApiProfileBean loadWfmApiProfileBean();
    SdApiProfileBean getWfmApiProfileBean();

    @SuppressWarnings("unused") // used in SdCacheEnum
    SdApiProfileBean loadUtApiProfileBean();
    SdApiProfileBean getUtApiProfileBean();

    @SuppressWarnings("unused") // used in SdCacheEnum
    SdApiProfileBean loadOssApiProfileBean();
    SdApiProfileBean getOssApiProfileBean();

    @SuppressWarnings("unused") // used in SdCacheEnum
    SdApiProfileBean loadGmbApiProfileBean();
    SdApiProfileBean getGmbApiProfileBean();
}
