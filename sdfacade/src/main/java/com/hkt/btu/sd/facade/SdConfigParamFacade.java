package com.hkt.btu.sd.facade;


import com.hkt.btu.sd.facade.data.SdConfigParamData;

import java.util.List;
import java.util.Optional;

public interface SdConfigParamFacade {
    List<SdConfigParamData> getAllConfigParam();

    Optional<SdConfigParamData> getConfigParamByGroupAndKey(String configGroup, String configKey);

    List<String> getConfigTypeList();

    boolean updateConfigParam(String configGroup, String configKey, String configValue, String configValueType);

    List<String> getConfigGroupList();

    boolean createConfigParam(String configGroup, String configKey, String configValue, String configValueType);

    boolean checkConfigKey(String configGroup, String configKey);
}
