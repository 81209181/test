package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.service.NorarsApiService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;

public class NorarsApiServiceImpl implements NorarsApiService {
    @Override
    public SiteInterfaceBean getNorarsRestfulApiBean() {
        SiteInterfaceBean bean =new SiteInterfaceBean();
        bean.setSystemName("NORARS");
        bean.setUrl("https://10.252.15.139");
        bean.setUserName("sd");
        bean.setPassword("g$Hw0#MG-3");
        return bean;
    }
}
