package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.service.BesApiService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;

public class BesApiServiceImpl implements BesApiService {
    @Override
    public SiteInterfaceBean getBesApiBean() {
        SiteInterfaceBean bean =new SiteInterfaceBean();
        bean.setSystemName("BES");
        bean.setUrl("https://10.50.36.110:2443");
        bean.setUserName("999007");
        bean.setPassword("Bearer 5b4bf68e5dae6376ac9d569b31b09800");
        bean.setxAppkey("16fdcfc5512645ebab925357499928ff");
        bean.setBeId("101");
        bean.setChannelType("613");
        return bean;
    }
}
