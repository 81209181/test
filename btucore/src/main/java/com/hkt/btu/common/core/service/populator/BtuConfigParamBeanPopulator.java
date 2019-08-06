package com.hkt.btu.common.core.service.populator;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;

public class BtuConfigParamBeanPopulator extends AbstractBeanPopulator<BtuConfigParamBean> {
    public void populate(BtuConfigParamEntity source, BtuConfigParamBean target) {
        super.populate(source, target);
        target.setConfigGroup(source.getConfigGroup());
        target.setConfigKey(source.getConfigKey());
        target.setConfigValueType(source.getConfigValueType());
        target.setConfigValue(source.getConfigValue());
    }
}
