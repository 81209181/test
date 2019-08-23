package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.service.ItsmApiService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;

public class ItsmApiServiceImpl implements ItsmApiService {
    @Override
    public SiteInterfaceBean getItsmRestfulApiBean() {
        SiteInterfaceBean bean =new SiteInterfaceBean();
        bean.setSystemName("ITSM_RESTFUL");
        bean.setUrl("https://10.111.7.32");
        bean.setUserName("ssa");
        bean.setPassword("sa2018");
        return bean;
    }

    @Override
    public SiteInterfaceBean getItsmSiteBean() {
        SiteInterfaceBean bean =new SiteInterfaceBean();
        bean.setSystemName("ITSM");
        bean.setUrl("https://10.111.7.32/itsm");
        return bean;
    }
}
