package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.dao.mapper.SdConfigParamMapper;
import com.hkt.btu.sd.core.service.ApiService;
import com.hkt.btu.sd.core.service.ItsmApiService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;

import javax.annotation.Resource;
import java.util.List;

public class ItsmApiServiceImpl extends ApiService implements ItsmApiService {

    @Resource
    private SdConfigParamMapper sdConfigParamMapper;

    @Override
    public SiteInterfaceBean getItsmRestfulApiBean() {
        List<SdConfigParamEntity> entities = sdConfigParamMapper.getValuesByConfigGroup(SiteInterfaceBean.ITSM_RESTFUL.API_NAME);
        return getSiteInterfaceBean(entities);
    }

    @Override
    public SiteInterfaceBean getItsmSiteBean() {
        List<SdConfigParamEntity> entities = sdConfigParamMapper.getValuesByConfigGroup(SiteInterfaceBean.ITSM.API_NAME);
        return getSiteInterfaceBean(entities);
    }
}
