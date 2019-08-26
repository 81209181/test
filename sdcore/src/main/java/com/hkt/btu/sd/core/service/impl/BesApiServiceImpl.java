package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.dao.mapper.SdConfigParamMapper;
import com.hkt.btu.sd.core.service.BesApiService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;
import com.hkt.btu.sd.core.service.populator.SdSiteInterfaceBeanPopulator;

import javax.annotation.Resource;
import java.util.List;

public class BesApiServiceImpl implements BesApiService {

    @Resource
    private SdConfigParamMapper sdConfigParamMapper;
    @Resource(name = "siteInterfaceBeanPopulator")
    SdSiteInterfaceBeanPopulator sdSiteInterfaceBeanPopulator;

    @Override
    public SiteInterfaceBean getBesApiBean() {
        SiteInterfaceBean bean =new SiteInterfaceBean();
        List<SdConfigParamEntity> entities = sdConfigParamMapper.getValuesByConfigGroup(SiteInterfaceBean.BES.SYSTEM_NAME);
        sdSiteInterfaceBeanPopulator.populate(entities,bean);
        return bean;
    }
}
