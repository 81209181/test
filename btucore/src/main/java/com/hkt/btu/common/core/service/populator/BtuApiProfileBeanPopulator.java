package com.hkt.btu.common.core.service.populator;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.service.bean.BtuApiProfileBean;

import java.util.Map;

public class BtuApiProfileBeanPopulator extends AbstractBeanPopulator<BtuApiProfileBean> {
    public void populate(Map<String, Object> configParamGroupMap, BtuApiProfileBean target){
        target.setSystemName((String) configParamGroupMap.get(BtuConfigParamEntity.API.CONFIG_KEY.SYSTEM_NAME));
        target.setUrl((String) configParamGroupMap.get(BtuConfigParamEntity.API.CONFIG_KEY.URL));
        target.setUserName((String) configParamGroupMap.get(BtuConfigParamEntity.API.CONFIG_KEY.USER_NAME));
        target.setPassword((String) configParamGroupMap.get(BtuConfigParamEntity.API.CONFIG_KEY.PASSWORD));
    }
}
