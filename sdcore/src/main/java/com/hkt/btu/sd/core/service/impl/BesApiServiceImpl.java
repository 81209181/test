package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.dao.mapper.SdConfigParamMapper;
import com.hkt.btu.sd.core.service.ApiService;
import com.hkt.btu.sd.core.service.BesApiService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;

import javax.annotation.Resource;
import java.util.List;

public class BesApiServiceImpl extends ApiService implements BesApiService {

    @Resource
    private SdConfigParamMapper sdConfigParamMapper;

    @Override
    public SiteInterfaceBean getBesApiBean() {
        List<SdConfigParamEntity> entities = sdConfigParamMapper.getValuesByConfigGroup(SiteInterfaceBean.BES.API_NAME);
        return getSiteInterfaceBean(entities);
    }
}
