package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;


public interface ApiService {
    SiteInterfaceBean getSiteInterfaceBean(String apiName);

    void reloadAllCached();
    void reloadCached(String apiName);
}
