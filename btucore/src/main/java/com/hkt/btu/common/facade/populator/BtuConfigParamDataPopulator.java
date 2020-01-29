package com.hkt.btu.common.facade.populator;

import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;
import com.hkt.btu.common.facade.data.BtuConfigParamData;

public class BtuConfigParamDataPopulator extends AbstractDataPopulator<BtuConfigParamData> {
    public void populate(BtuConfigParamBean source, BtuConfigParamData target) {
        target.setConfigGroup(source.getConfigGroup());
        target.setConfigKey(source.getConfigKey());
        target.setConfigValueType(source.getConfigValueType().getTypeCode());
        target.setConfigValue(source.getConfigValue());
        target.setEncrypt(source.isEncrypt());
        target.setModifydate(source.getModifydate());
    }
}
