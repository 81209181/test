package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.service.BtuConfigParamService;
import com.hkt.btu.sd.core.service.bean.SdConfigParamBean;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;

public interface SdConfigParamService extends BtuConfigParamService {
    List<SdConfigParamBean> getAllConfigParam();

    Optional<SdConfigParamBean> getConfigParamByGroupAndKey(String configGroup, String configKey);

    boolean updateConfigParam(String configGroup, String configKey, String configValue, String configValueType, String encrypt) throws GeneralSecurityException;

    List<String> getConfigGroupList();

    boolean createConfigParam(String configGroup, String configKey, String configValue, String configValueType, String encrypt) throws GeneralSecurityException;

    boolean checkConfigKey(String configGroup, String configKey);

    boolean checkConfigParam(String configGroup, String configKey, String configValue, String configValueType);
}
