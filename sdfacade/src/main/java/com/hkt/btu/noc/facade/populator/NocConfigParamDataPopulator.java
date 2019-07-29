package com.hkt.btu.noc.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.noc.core.service.bean.NocConfigParamBean;
import com.hkt.btu.noc.facade.data.NocConfigParamData;

public class NocConfigParamDataPopulator extends AbstractDataPopulator<NocConfigParamData> {

    public void populate(NocConfigParamBean source, NocConfigParamData target) {
        target.setConfigGroup(source.getConfigGroup());
        target.setConfigKey(source.getConfigKey());
        target.setConfigValueType(source.getConfigValueType());
        target.setConfigValue(source.getConfigValue());

        target.setModifydate(source.getModifydate());
    }
}
