package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;
import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.service.bean.SdConfigParamBean;
import org.apache.commons.lang3.StringUtils;

public class SdConfigParamBeanPopulator extends AbstractBeanPopulator<SdConfigParamBean> {

    public void populate(SdConfigParamEntity source, SdConfigParamBean target) {
        super.populate(source, target);

        target.setConfigGroup(source.getConfigGroup());
        target.setConfigKey(source.getConfigKey());
        target.setConfigValueType(source.getConfigValueType());
        if (StringUtils.equals(source.getEncrypt(), SdConfigParamEntity.ENCRYPT.YES)) {
            target.setEncrypt(true);
            target.setConfigValue(BtuConfigParamBean.CONFIG_VALUE.ENCRYPTED);
        } else {
            target.setEncrypt(false);
            target.setConfigValue(source.getConfigValue());
        }
    }
}