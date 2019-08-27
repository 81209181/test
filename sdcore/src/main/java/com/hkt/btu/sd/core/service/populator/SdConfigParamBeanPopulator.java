package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.service.BtuSensitiveDataService;
import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.service.bean.SdConfigParamBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;

import javax.annotation.Resource;

public class SdConfigParamBeanPopulator extends AbstractBeanPopulator<SdConfigParamBean> {

    @Resource(name = "sensitiveDataService")
    BtuSensitiveDataService sensitiveDataService;

    public void populate(SdConfigParamEntity source, SdConfigParamBean target) {
        super.populate(source, target);

        target.setConfigGroup(source.getConfigGroup());
        target.setConfigKey(source.getConfigKey());
        target.setConfigValueType(source.getConfigValueType());
        if (StringUtils.isNotEmpty(source.getEncrypt()) && source.getEncrypt().equalsIgnoreCase(SdConfigParamEntity.ENCRYPT.Y)) {
            target.setEncrypt(true);
            target.setConfigValue(BtuConfigParamEntity.ENCRYPT.ENCRYPTED);
        } else {
            target.setEncrypt(false);
            target.setConfigValue(source.getConfigValue());
        }
    }

    public void populate4EditConfigParam(SdConfigParamEntity source, SdConfigParamBean target) {
        super.populate(source, target);
        target.setConfigGroup(source.getConfigGroup());
        target.setConfigKey(source.getConfigKey());
        target.setConfigValueType(source.getConfigValueType());
        if (StringUtils.isNotEmpty(source.getEncrypt()) && source.getEncrypt().equalsIgnoreCase(SdConfigParamEntity.ENCRYPT.Y)) {
            target.setEncrypt(true);
            target.setConfigValue(sensitiveDataService.decryptToStringSafe(Base64Utils.decodeFromString(source.getConfigValue())));
        } else {
            target.setEncrypt(false);
            target.setConfigValue(source.getConfigValue());
        }
    }

}