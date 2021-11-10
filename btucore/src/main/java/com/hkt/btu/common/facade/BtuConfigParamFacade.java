package com.hkt.btu.common.facade;

import com.hkt.btu.common.facade.data.BtuConfigParamData;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;


public interface BtuConfigParamFacade {
    List<BtuConfigParamData> getAllConfigParam();
    Optional<BtuConfigParamData> getConfigParamByGroupAndKey(String configGroup, String configKey);

    List<String> getConfigTypeList();
    boolean createConfigParam(String configGroup, String configKey, String configValue, String configValueType,
                              String encrypt) throws GeneralSecurityException;
    boolean updateConfigParam(String configGroup, String configKey, String configValue, String configValueType,
                              String encrypt) throws GeneralSecurityException;

    List<String> getConfigGroupList();



    boolean checkConfigKey(String configGroup, String configKey);

    boolean checkConfigParam(String configGroup, String configKey, String configValue, String configValueType);
}
