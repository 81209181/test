package com.hkt.btu.common.core.dao.entity;

public class BtuConfigParamEntity extends BaseEntity {

    public static class TYPE {
        public static final String STRING = "String";
        public static final String INTEGER = "Integer";
        public static final String DOUBLE = "Double";
        public static final String BOOLEAN = "Boolean";
        public static final String LOCAL_DATE_TIME = "LocalDateTime";
    }

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
