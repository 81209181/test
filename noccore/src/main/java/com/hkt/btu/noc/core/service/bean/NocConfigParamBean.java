package com.hkt.btu.noc.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;


public class NocConfigParamBean extends BaseBean {
    private String configGroup;
    private String configKey;
    private String configValue;
    private String configValueType;


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

    public String getConfigValueType() {
        return configValueType;
    }

    public void setConfigValueType(String configValueType) {
        this.configValueType = configValueType;
    }
}
