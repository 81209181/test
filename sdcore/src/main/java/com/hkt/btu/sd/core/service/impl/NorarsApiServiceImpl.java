package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.dao.mapper.SdConfigParamMapper;
import com.hkt.btu.sd.core.service.ApiService;
import com.hkt.btu.sd.core.service.NorarsApiService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;

import javax.annotation.Resource;
import java.util.List;

public class NorarsApiServiceImpl extends ApiService implements NorarsApiService {

    @Resource
    private SdConfigParamMapper sdConfigParamMapper;

    @Override
    public SiteInterfaceBean getNorarsRestfulApiBean() {
        List<SdConfigParamEntity> entities = sdConfigParamMapper.getValuesByConfigGroup(SiteInterfaceBean.NORARS.SYSTEM_NAME);
        return getSiteInterfaceBean(entities);
    }
}
