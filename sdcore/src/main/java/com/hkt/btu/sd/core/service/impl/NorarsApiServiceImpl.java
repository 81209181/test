package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.dao.mapper.SdConfigParamMapper;
import com.hkt.btu.sd.core.service.NorarsApiService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;
import com.hkt.btu.sd.core.service.populator.SdSiteInterfaceBeanPopulator;

import javax.annotation.Resource;
import java.util.List;

public class NorarsApiServiceImpl implements NorarsApiService {

    @Resource
    private SdConfigParamMapper sdConfigParamMapper;
    @Resource(name = "siteInterfaceBeanPopulator")
    SdSiteInterfaceBeanPopulator sdSiteInterfaceBeanPopulator;

    @Override
    public SiteInterfaceBean getNorarsRestfulApiBean() {
        SiteInterfaceBean bean =new SiteInterfaceBean();
        List<SdConfigParamEntity> entities = sdConfigParamMapper.getValuesByConfigGroup(SiteInterfaceBean.NORARS.SYSTEM_NAME);
        sdSiteInterfaceBeanPopulator.populate(entities,bean);
        return bean;
    }
}
