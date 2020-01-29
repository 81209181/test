package com.hkt.btu.common.core.service.bean;


import com.hkt.btu.common.core.service.constant.BtuConfigParamTypeEnum;

public class BtuConfigParamBean extends BaseBean {

    public static class CONFIG_VALUE {
        public static final String ENCRYPTED = "ENCRYPTED";
    }

    private String configGroup;
    private String configKey;
    private String configValue;
    private String configValueRaw;
    private BtuConfigParamTypeEnum configValueType;
    private boolean encrypt;


    public String getConfigGroup() {
        return configGroup;
    }

    public void setConfigGroup(String configGroup) {
        this.configGroup = configGroup;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigValueRaw() {
        return configValueRaw;
    }

    public void setConfigValueRaw(String configValueRaw) {
        this.configValueRaw = configValueRaw;
    }

    public BtuConfigParamTypeEnum getConfigValueType() {
        return configValueType;
    }

    public void setConfigValueType(BtuConfigParamTypeEnum configValueType) {
        this.configValueType = configValueType;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }
}
