package com.hkt.btu.common.core.service;


import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;
import com.hkt.btu.common.core.service.constant.BtuConfigParamTypeEnum;

import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface BtuConfigParamService {

    boolean createConfigParam(String configGroup, String configKey, String configValue, BtuConfigParamTypeEnum configValueType,
                              String encrypt) throws GeneralSecurityException;

    boolean updateConfigParam(String configGroup, String configKey, String configValue, BtuConfigParamTypeEnum configValueType,
                              String encrypt) throws GeneralSecurityException;

    // get all
    List<String> getConfigTypeList();
    List<BtuConfigParamBean> getAllConfigParam();

    // get by config group
    List<String> getConfigGroupList();
    Map<String, Object> getConfigParamByConfigGroup(String configGroup, boolean decrypt);

    // get by config group and key
    BtuConfigParamBean getConfigParamByGroupAndKey(String configGroup, String configKey);
    List<BtuConfigParamBean> getConfigParamBeanListInternal(String configGroup, String configKey);

    // config value type
    String getString(String configGroup, String configKey);
    Integer getInteger(String configGroup, String configKey);
    Double getDouble(String configGroup, String configKey);
    Boolean getBoolean(String configGroup, String configKey);
    LocalDateTime getDateTime(String configGroup, String configKey);

    // checking
    boolean checkConfigParamInput(String configGroup, String configKey, String configValue, BtuConfigParamTypeEnum configValueType);
    boolean checkConfigKey(String configGroup, String configKey);
}
