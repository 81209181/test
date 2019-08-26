package com.hkt.btu.sd.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdConfigParamBean;
import com.hkt.btu.sd.facade.data.SdConfigParamData;

public class SdConfigParamDataPopulator extends AbstractDataPopulator<SdConfigParamData> {

    public void populate(SdConfigParamBean source, SdConfigParamData target) {
        target.setConfigGroup(source.getConfigGroup());
        target.setConfigKey(source.getConfigKey());
        target.setConfigValueType(source.getConfigValueType());
        target.setConfigValue(source.getConfigValue());
        target.setEncrypt(source.getEncrypt());
        target.setModifydate(source.getModifydate());
    }
}
