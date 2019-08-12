package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.service.BtuConfigParamService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

public class BtuConfigParamServiceImpl implements BtuConfigParamService {
    private static final Logger LOG = LogManager.getLogger(BtuConfigParamServiceImpl.class);

    @Override
    public List<String> getConfigTypeList() {
        return BtuConfigParamEntity.getConfigTypeList();
    }

    @Override
    public Map<String, Object> getConfigParamByConfigGroup(String configGroup) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        return null;
    }

    protected BtuConfigParamEntity getConfigParamEntity(String configGroup, String configKey, String configValueType) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        return null;
    }

    protected Object getValue(BtuConfigParamEntity sdConfigParamEntity) {
        if (sdConfigParamEntity == null) {
            return null;
        }

        String type = sdConfigParamEntity.getConfigValueType();
        String value = sdConfigParamEntity.getConfigValue();

        try {
            if (BtuConfigParamEntity.TYPE.STRING.equals(type)) {
                return value;
            } else if (BtuConfigParamEntity.TYPE.BOOLEAN.equals(type)) {
                return BooleanUtils.toBoolean(value);
            } else if (BtuConfigParamEntity.TYPE.LOCAL_DATE_TIME.equals(type)) {
                return LocalDateTime.parse(value);
            } else if (BtuConfigParamEntity.TYPE.INTEGER.equals(type)) {
                return Integer.parseInt(value);
            } else if (BtuConfigParamEntity.TYPE.DOUBLE.equals(type)) {
                return Double.parseDouble(value);
            }
        } catch (DateTimeParseException | NullPointerException | NumberFormatException e) {
            LOG.warn(e.getMessage());
        }

        LOG.warn(String.format("Cannot parse %s to %s.", value, type));
        return null;
    }

    @Override
    public String getString(String configGroup, String configKey) {
        BtuConfigParamEntity btuConfigParamEntity = getConfigParamEntity(configGroup, configKey, BtuConfigParamEntity.TYPE.STRING);
        return (String) getValue(btuConfigParamEntity);
    }

    @Override
    public Integer getInteger(String configGroup, String configKey) {
        BtuConfigParamEntity sdConfigParamEntity = getConfigParamEntity(configGroup, configKey, BtuConfigParamEntity.TYPE.INTEGER);
        return (Integer) getValue(sdConfigParamEntity);
    }

    @Override
    public Double getDouble(String configGroup, String configKey) {
        BtuConfigParamEntity sdConfigParamEntity = getConfigParamEntity(configGroup, configKey, BtuConfigParamEntity.TYPE.DOUBLE);
        return (Double) getValue(sdConfigParamEntity);
    }

    @Override
    public Boolean getBoolean(String configGroup, String configKey) {
        BtuConfigParamEntity sdConfigParamEntity = getConfigParamEntity(configGroup, configKey, BtuConfigParamEntity.TYPE.BOOLEAN);
        return (Boolean) getValue(sdConfigParamEntity);
    }

    @Override
    public LocalDateTime getDateTime(String configGroup, String configKey) {
        BtuConfigParamEntity sdConfigParamEntity = getConfigParamEntity(configGroup, configKey, BtuConfigParamEntity.TYPE.LOCAL_DATE_TIME);
        return (LocalDateTime) getValue(sdConfigParamEntity);
    }
}
