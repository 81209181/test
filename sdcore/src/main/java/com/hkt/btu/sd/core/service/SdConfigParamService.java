package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.service.BtuConfigParamService;
import com.hkt.btu.sd.core.service.bean.SdConfigParamBean;

import java.util.List;
import java.util.Optional;

public interface SdConfigParamService extends BtuConfigParamService {
    List<SdConfigParamBean> getAllConfigParam();

    Optional<SdConfigParamBean> getConfigParamByGroupAndKey(String configGroup, String configKey);

    boolean updateConfigParam(String configGroup, String configKey, String configValue, String configValueType);
}
