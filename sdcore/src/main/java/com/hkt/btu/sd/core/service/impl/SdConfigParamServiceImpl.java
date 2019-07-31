package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.dao.mapper.SdConfigParamMapper;
import com.hkt.btu.sd.core.service.SdConfigParamService;
import com.hkt.btu.sd.core.service.bean.SdConfigParamBean;
import com.hkt.btu.sd.core.service.populator.SdConfigParamBeanPopulator;
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

public class SdConfigParamServiceImpl implements SdConfigParamService {
    private static final Logger LOG = LogManager.getLogger(SdConfigParamServiceImpl.class);


    @Resource
    private SdConfigParamMapper sdConfigParamMapper;

    @Resource(name = "configParamBeanPopulator")
    SdConfigParamBeanPopulator sdConfigParamBeanPopulator;

    public Map<String, Object> getConfigParamByConfigGroup(String configGroup) {
        if(StringUtils.isEmpty(configGroup)){
            LOG.warn("Empty config group input.");
            return null;
        }

        HashMap<String, Object> map = new HashMap<>();

        List<SdConfigParamEntity> configEntityList = sdConfigParamMapper.getValuesByConfigGroup(configGroup);
        for(SdConfigParamEntity e : configEntityList){
            Object obj = getValue(e);
            map.put(e.getConfigKey(), obj);
        }
        return map;
    }

    public String getString(String configGroup, String configKey) {
        SdConfigParamEntity sdConfigParamEntity = getConfigParamEntity(configGroup, configKey, SdConfigParamEntity.TYPE.STRING);
        return (String) getValue(sdConfigParamEntity);
    }

    public Integer getInteger(String configGroup, String configKey) {
        SdConfigParamEntity sdConfigParamEntity = getConfigParamEntity(configGroup, configKey, SdConfigParamEntity.TYPE.INTEGER);
        return (Integer) getValue(sdConfigParamEntity);
    }

    public Double getDouble(String configGroup, String configKey) {
        SdConfigParamEntity sdConfigParamEntity = getConfigParamEntity(configGroup, configKey, SdConfigParamEntity.TYPE.DOUBLE);
        return (Double) getValue(sdConfigParamEntity);
    }

    public Boolean getBoolean(String configGroup, String configKey) {
        SdConfigParamEntity sdConfigParamEntity = getConfigParamEntity(configGroup, configKey, SdConfigParamEntity.TYPE.BOOLEAN);
        return (Boolean) getValue(sdConfigParamEntity);
    }

    public LocalDateTime getDateTime(String configGroup, String configKey) {
        SdConfigParamEntity sdConfigParamEntity = getConfigParamEntity(configGroup, configKey, SdConfigParamEntity.TYPE.LOCAL_DATE_TIME);
        return (LocalDateTime) getValue(sdConfigParamEntity);
    }

    @Override
    public List<SdConfigParamBean> getAllConfigParam() {
        List<SdConfigParamEntity> entityList = sdConfigParamMapper.getAllConfigParam();
        if(CollectionUtils.isEmpty(entityList)){
           return null;
        }

        List<SdConfigParamBean> beanList = new LinkedList<>();
        for(SdConfigParamEntity entity : entityList){
            SdConfigParamBean bean = new SdConfigParamBean();
            sdConfigParamBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }

        return beanList;
    }

    private Object getValue(SdConfigParamEntity sdConfigParamEntity){
        if (sdConfigParamEntity == null) {
            return null;
        }

        String type = sdConfigParamEntity.getConfigValueType();
        String value = sdConfigParamEntity.getConfigValue();

        try {
            if (SdConfigParamEntity.TYPE.STRING.equals(type)) {
                return value;
            } else if (SdConfigParamEntity.TYPE.BOOLEAN.equals(type)) {
                return BooleanUtils.toBoolean(value);
            } else if (SdConfigParamEntity.TYPE.LOCAL_DATE_TIME.equals(type)) {
                return LocalDateTime.parse(value);
            } else if (SdConfigParamEntity.TYPE.INTEGER.equals(type)) {
                return Integer.parseInt(value);
            } else if (SdConfigParamEntity.TYPE.DOUBLE.equals(type)) {
                return Double.parseDouble(value);
            }
        } catch (DateTimeParseException | NullPointerException | NumberFormatException e){
            LOG.warn(e.getMessage());
        }

        LOG.warn(String.format("Cannot parse %s to %s.", value, type));
        return null;
    }

    private SdConfigParamEntity getConfigParamEntity(String configGroup, String configKey, String configValueType) {
        if (StringUtils.isEmpty(configGroup)) {
            LOG.warn("Empty config group input.");
            return null;
        } else if (StringUtils.isEmpty(configKey)) {
            LOG.warn("Empty config key input.");
            return null;
        }

        SdConfigParamEntity configEntity = sdConfigParamMapper.getValue(configGroup, configKey);
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
}
