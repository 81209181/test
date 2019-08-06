package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.service.bean.SdConfigParamBean;

public class SdConfigParamBeanPopulator extends AbstractBeanPopulator<SdConfigParamBean> {
    public void populate(SdConfigParamEntity source, SdConfigParamBean target) {
        super.populate(source, target);

        target.setConfigGroup(source.getConfigGroup());
        target.setConfigKey(source.getConfigKey());
        target.setConfigValueType(source.getConfigValueType());
        target.setConfigValue(source.getConfigValue());
    }

}