package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.service.BtuApiService;
import com.hkt.btu.sd.core.service.bean.SdApiProfileBean;


public interface SdApiService extends BtuApiService {
    SdApiProfileBean loadBesApiProfileBean();
    SdApiProfileBean getBesApiProfileBean();

    SdApiProfileBean loadItsmApiProfileBean();
    SdApiProfileBean getItsmApiProfileBean();

    SdApiProfileBean loadItsmRestfulApiProfileBean();
    SdApiProfileBean getItsmRestfulApiProfileBean();

    SdApiProfileBean loadNorarsApiProfileBean();
    SdApiProfileBean getNorarsApiProfileBean();

    SdApiProfileBean loadWfmApiProfileBean();
    SdApiProfileBean getWfmApiProfileBean();

    SdApiProfileBean loadUtApiProfileBean();
    SdApiProfileBean getUtApiProfileBean();
}
