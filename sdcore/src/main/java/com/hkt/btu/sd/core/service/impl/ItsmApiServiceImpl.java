package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.dao.mapper.SdConfigParamMapper;
import com.hkt.btu.sd.core.service.ItsmApiService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;
import com.hkt.btu.sd.core.service.populator.SdSiteInterfaceBeanPopulator;

import javax.annotation.Resource;
import java.util.List;

public class ItsmApiServiceImpl implements ItsmApiService {

    @Resource
    private SdConfigParamMapper sdConfigParamMapper;
    @Resource(name = "siteInterfaceBeanPopulator")
    SdSiteInterfaceBeanPopulator siteInterfaceBeanPopulator;

    @Override
    public SiteInterfaceBean getItsmRestfulApiBean() {
        SiteInterfaceBean bean =new SiteInterfaceBean();
        List<SdConfigParamEntity> entities = sdConfigParamMapper.getValuesByConfigGroup(SiteInterfaceBean.ITSM_RESTFUL.SYSTEM_NAME);
        siteInterfaceBeanPopulator.populate(entities,bean);
        return bean;
    }

    @Override
    public SiteInterfaceBean getItsmSiteBean() {
        SiteInterfaceBean bean =new SiteInterfaceBean();
        List<SdConfigParamEntity> entities = sdConfigParamMapper.getValuesByConfigGroup(SiteInterfaceBean.ITSM.SYSTEM_NAME);
        siteInterfaceBeanPopulator.populate(entities,bean);
        return bean;
    }
}
