package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.service.BtuApiClientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BtuApiClientServiceImpl implements BtuApiClientService {

    private static final Logger LOG = LogManager.getLogger(BtuApiClientServiceImpl.class);

    @Override
    public String getApiClientBean(String apiName) {
        LOG.warn("Cannot get api client in btu service layer.");
        throw new NullPointerException("Cannot get api client in btu service layer.");
    }

    @Override
    public void reloadCached(String apiName) {
        LOG.info(apiName + " reload successfully.");
    }
}
