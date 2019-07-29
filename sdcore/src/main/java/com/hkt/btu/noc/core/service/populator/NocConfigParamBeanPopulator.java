package com.hkt.btu.noc.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.noc.core.dao.entity.NocConfigParamEntity;
import com.hkt.btu.noc.core.service.bean.NocConfigParamBean;

public class NocConfigParamBeanPopulator extends AbstractBeanPopulator<NocConfigParamBean> {
    public void populate(NocConfigParamEntity source, NocConfigParamBean target) {
        super.populate(source, target);

        target.setConfigGroup(source.getConfigGroup());
        target.setConfigKey(source.getConfigKey());
        target.setConfigValueType(source.getConfigValueType());
        target.setConfigValue(source.getConfigValue());
    }

}