package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.service.BtuConfigParamService;
import com.hkt.btu.sd.core.service.bean.SdConfigParamBean;

import java.util.List;

public interface SdConfigParamService extends BtuConfigParamService {
    List<SdConfigParamBean> getAllConfigParam();
}
