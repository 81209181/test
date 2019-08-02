package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.dao.mapper.BtuConfigParamMapper;
import com.hkt.btu.common.core.service.BtuConfigParamService;
import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;
import com.hkt.btu.common.core.service.populator.BtuConfigParamBeanPopulator;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BtuConfigParamServiceImpl implements BtuConfigParamService {
    private static final Logger LOG = LogManager.getLogger(BtuConfigParamServiceImpl.class);

    @Resource
    BtuConfigParamMapper configParamMapper;

    @Resource(name = "configParamBeanPopulator")
    BtuConfigParamBeanPopulator sdConfigParamBeanPopulator;

    @Override
    public Map<String, Object> getConfigParamByConfigGroup(String configGroup) {
        if (StringUtils.isEmpty(configGroup)) {
            LOG.warn("Empty config group input.");
            return null;
        }

        HashMap<String, Object> map = new HashMap<>();

        List<BtuConfigParamEntity> configEntityList = configParamMapper.getValuesByConfigGroup(configGroup);
        for (BtuConfigParamEntity e : configEntityList) {
            Object obj = getValue(e);
            map.put(e.getConfigKey(), obj);
        }
        return map;

    }

    private Object getValue(BtuConfigParamEntity btuConfigParamEntity) {
        if (btuConfigParamEntity == null) {
            return null;
        }

        String type = btuConfigParamEntity.getConfigValueType();
        String value = btuConfigParamEntity.getConfigValue();

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
        BtuConfigParamEntity sdConfigParamEntity = getConfigParamEntity(configGroup, configKey, BtuConfigParamEntity.TYPE.STRING);
        return (String) getValue(sdConfigParamEntity);
    }

    private BtuConfigParamEntity getConfigParamEntity(String configGroup, String configKey, String configValueType) {
        if (StringUtils.isEmpty(configGroup)) {
            LOG.warn("Empty config group input.");
            return null;
        } else if (StringUtils.isEmpty(configKey)) {
            LOG.warn("Empty config key input.");
            return null;
        }

        BtuConfigParamEntity configEntity = configParamMapper.getValue(configGroup, configKey);
        if (configEntity == null) {
            LOG.warn("ConfigParam not found; configGroup: " + configGroup + ", configKey: " + configKey + ".");
            return null;
        } else if ( ! StringUtils.equals(configValueType, configEntity.getConfigValueType()) ) {
            LOG.warn("ConfigParam value type should be " + configValueType +
                    " instead of " + configEntity.getConfigValueType() + ".");
            return null;
        }

        return configEntity;

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

    @Override
    public List<BtuConfigParamBean> getAllConfigParam() {
        List<BtuConfigParamEntity> entityList = configParamMapper.getAllConfigParam();
        if(CollectionUtils.isEmpty(entityList)){
            return null;
        }

        List<BtuConfigParamBean> beanList = new LinkedList<>();
        for(BtuConfigParamEntity entity : entityList){
            BtuConfigParamBean bean = new BtuConfigParamBean();
            sdConfigParamBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }

        return beanList;

    }
}
