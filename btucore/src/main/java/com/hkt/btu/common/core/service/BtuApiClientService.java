package com.hkt.btu.common.core.service;

import java.util.Map;

public interface BtuApiClientService {

    Map<String, Object> loadApiClientBean();

    String getApiClientBean(String apiName);
}
