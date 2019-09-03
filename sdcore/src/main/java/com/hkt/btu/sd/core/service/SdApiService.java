package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;


public interface SdApiService {
    SiteInterfaceBean getSiteInterfaceBean(String apiName);

    void reloadAllCached();
    void reloadCached(String apiName);
}
