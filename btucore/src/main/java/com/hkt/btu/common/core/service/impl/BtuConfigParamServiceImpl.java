package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.service.BtuConfigParamService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class BtuConfigParamServiceImpl implements BtuConfigParamService {
    private static final Logger LOG = LogManager.getLogger(BtuConfigParamServiceImpl.class);

    @Override
    public Map<String, Object> getConfigParamByConfigGroup(String configGroup) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");

        if(StringUtils.isEmpty(configGroup)){
            LOG.warn("Empty config group input.");
            return null;
        }
        HashMap<String, Object> map = new HashMap<>();
        return null;
    }

    @Override
    public String getString(String configGroup, String configKey) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        return null;
    }

    @Override
    public Integer getInteger(String configGroup, String configKey) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        return null;
    }

    @Override
    public Double getDouble(String configGroup, String configKey) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        return null;
    }

    @Override
    public Boolean getBoolean(String configGroup, String configKey) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        return null;
    }

    @Override
    public LocalDateTime getDateTime(String configGroup, String configKey) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        return null;
    }
}
