package com.hkt.btu.common.core.service;

import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface BtuConfigParamService {

    Map<String, Object> getConfigParamByConfigGroup(String configGroup);

    String getString(String configGroup, String configKey);
    Integer getInteger(String configGroup, String configKey);
    Double getDouble(String configGroup, String configKey);
    Boolean getBoolean(String configGroup, String configKey);
    LocalDateTime getDateTime(String configGroup, String configKey);

    List<BtuConfigParamBean> getAllConfigParam();
}
