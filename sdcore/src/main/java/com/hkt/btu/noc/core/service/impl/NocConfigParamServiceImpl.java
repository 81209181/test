package com.hkt.btu.noc.core.service.impl;

import com.hkt.btu.noc.core.dao.entity.NocConfigParamEntity;
import com.hkt.btu.noc.core.dao.mapper.NocConfigParamMapper;
import com.hkt.btu.noc.core.service.NocConfigParamService;
import com.hkt.btu.noc.core.service.bean.NocConfigParamBean;
import com.hkt.btu.noc.core.service.populator.NocConfigParamBeanPopulator;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service("NocConfigParamService")
public class NocConfigParamServiceImpl implements NocConfigParamService {
    private static final Logger LOG = LogManager.getLogger(NocConfigParamServiceImpl.class);


    @Resource
    private NocConfigParamMapper nocConfigParamMapper;

    @Autowired
    NocConfigParamBeanPopulator nocConfigParamBeanPopulator;

    public Map<String, Object> getConfigParamByConfigGroup(String configGroup) {
        if(StringUtils.isEmpty(configGroup)){
            LOG.warn("Empty config group input.");
            return null;
        }

        HashMap<String, Object> map = new HashMap<>();

        List<NocConfigParamEntity> configEntityList = nocConfigParamMapper.getValuesByConfigGroup(configGroup);
        for(NocConfigParamEntity e : configEntityList){
            Object obj = getValue(e);
            map.put(e.getConfigKey(), obj);
        }
        return map;
    }

    public String getString(String configGroup, String configKey) {
        NocConfigParamEntity nocConfigParamEntity = getConfigParamEntity(configGroup, configKey, NocConfigParamEntity.TYPE.STRING);
        return (String) getValue(nocConfigParamEntity);
    }

    public Integer getInteger(String configGroup, String configKey) {
        NocConfigParamEntity nocConfigParamEntity = getConfigParamEntity(configGroup, configKey, NocConfigParamEntity.TYPE.INTEGER);
        return (Integer) getValue(nocConfigParamEntity);
    }

    public Double getDouble(String configGroup, String configKey) {
        NocConfigParamEntity nocConfigParamEntity = getConfigParamEntity(configGroup, configKey, NocConfigParamEntity.TYPE.DOUBLE);
        return (Double) getValue(nocConfigParamEntity);
    }

    public Boolean getBoolean(String configGroup, String configKey) {
        NocConfigParamEntity nocConfigParamEntity = getConfigParamEntity(configGroup, configKey, NocConfigParamEntity.TYPE.BOOLEAN);
        return (Boolean) getValue(nocConfigParamEntity);
    }

    public LocalDateTime getDateTime(String configGroup, String configKey) {
        NocConfigParamEntity nocConfigParamEntity = getConfigParamEntity(configGroup, configKey, NocConfigParamEntity.TYPE.LOCAL_DATE_TIME);
        return (LocalDateTime) getValue(nocConfigParamEntity);
    }

    @Override
    public List<NocConfigParamBean> getAllConfigParam() {
        List<NocConfigParamEntity> entityList = nocConfigParamMapper.getAllConfigParam();
        if(CollectionUtils.isEmpty(entityList)){
           return null;
        }

        List<NocConfigParamBean> beanList = new LinkedList<>();
        for(NocConfigParamEntity entity : entityList){
            NocConfigParamBean bean = new NocConfigParamBean();
            nocConfigParamBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }

        return beanList;
    }

    private Object getValue(NocConfigParamEntity nocConfigParamEntity){
        if (nocConfigParamEntity == null) {
            return null;
        }

        String type = nocConfigParamEntity.getConfigValueType();
        String value = nocConfigParamEntity.getConfigValue();

        try {
            if (NocConfigParamEntity.TYPE.STRING.equals(type)) {
                return value;
            } else if (NocConfigParamEntity.TYPE.BOOLEAN.equals(type)) {
                return BooleanUtils.toBoolean(value);
            } else if (NocConfigParamEntity.TYPE.LOCAL_DATE_TIME.equals(type)) {
                return LocalDateTime.parse(value);
            } else if (NocConfigParamEntity.TYPE.INTEGER.equals(type)) {
                return Integer.parseInt(value);
            } else if (NocConfigParamEntity.TYPE.DOUBLE.equals(type)) {
                return Double.parseDouble(value);
            }
        } catch (DateTimeParseException | NullPointerException | NumberFormatException e){
            LOG.warn(e.getMessage());
        }

        LOG.warn(String.format("Cannot parse %s to %s.", value, type));
        return null;
    }

    private NocConfigParamEntity getConfigParamEntity(String configGroup, String configKey, String configValueType) {
        if (StringUtils.isEmpty(configGroup)) {
            LOG.warn("Empty config group input.");
            return null;
        } else if (StringUtils.isEmpty(configKey)) {
            LOG.warn("Empty config key input.");
            return null;
        }

        NocConfigParamEntity configEntity = nocConfigParamMapper.getValue(configGroup, configKey);
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
