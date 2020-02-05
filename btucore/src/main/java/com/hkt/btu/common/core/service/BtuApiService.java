package com.hkt.btu.common.core.service;

import com.hkt.btu.common.core.service.bean.BtuApiProfileBean;


public interface BtuApiService {
    // cache
    BtuApiProfileBean loadApiProfileBeanInternal(String apiName);
    BtuApiProfileBean getApiProfileBeanInternal(String apiName);
}
