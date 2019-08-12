package com.hkt.btu.sd.facade.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hkt.btu.common.facade.data.DataInterface;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class SdConfigParamData implements DataInterface {
    private String configGroup;
    private String configKey;
    private String configValue;
    private String configValueType;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime modifydate;


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

    public LocalDateTime getModifydate() {
        return modifydate;
    }

    public void setModifydate(LocalDateTime modifydate) {
        this.modifydate = modifydate;
    }
}
