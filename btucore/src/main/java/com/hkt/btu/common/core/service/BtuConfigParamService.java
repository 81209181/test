package com.hkt.btu.common.core.service;


import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;

import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BtuConfigParamService {

    boolean createConfigParam(String configGroup, String configKey, String configValue, String configValueType, String encrypt) throws GeneralSecurityException;

    boolean updateConfigParam(String configGroup, String configKey, String configValue, String configValueType, String encrypt) throws GeneralSecurityException;


    List<String> getConfigTypeList();

    Map<String, Object> getConfigParamByConfigGroup(String configGroup, boolean decrypt);
    Optional<BtuConfigParamBean> getConfigParamByGroupAndKey(String configGroup, String configKey);

    String getString(String configGroup, String configKey);
    Integer getInteger(String configGroup, String configKey);
    Double getDouble(String configGroup, String configKey);
    Boolean getBoolean(String configGroup, String configKey);
    LocalDateTime getDateTime(String configGroup, String configKey);
}
