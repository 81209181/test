package com.hkt.btu.common.core.service.populator;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;
import com.hkt.btu.common.core.service.constant.BtuConfigParamTypeEnum;
import org.apache.commons.lang3.StringUtils;

public class BtuConfigParamBeanPopulator extends AbstractBeanPopulator<BtuConfigParamBean>{
    public void populate(BtuConfigParamEntity source, BtuConfigParamBean target) {
        super.populate(source, target);

        target.setConfigGroup(source.getConfigGroup());
        target.setConfigKey(source.getConfigKey());
        target.setConfigValue(source.getConfigValue());
        target.setConfigValueRaw(source.getConfigValue());
        target.setConfigValueType(BtuConfigParamTypeEnum.getEnum(source.getConfigValueType()));

        if (StringUtils.equals(source.getEncrypt(), BtuConfigParamEntity.ENCRYPT.YES)) {
            target.setEncrypt(true);
            target.setConfigValue(BtuConfigParamBean.CONFIG_VALUE.ENCRYPTED);
        } else {
            target.setEncrypt(false);
        }
    }
}
