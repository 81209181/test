package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.service.impl.BtuConfigParamServiceImpl;
import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.dao.mapper.SdConfigParamMapper;
import com.hkt.btu.sd.core.service.SdConfigParamService;
import com.hkt.btu.sd.core.service.bean.SdConfigParamBean;
import com.hkt.btu.sd.core.service.populator.SdConfigParamBeanPopulator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SdConfigParamServiceImpl extends BtuConfigParamServiceImpl implements SdConfigParamService {
    private static final Logger LOG = LogManager.getLogger(SdConfigParamServiceImpl.class);


    @Resource
    private SdConfigParamMapper sdConfigParamMapper;

    @Resource(name = "configParamBeanPopulator")
    SdConfigParamBeanPopulator sdConfigParamBeanPopulator;

    public Map<String, Object> getConfigParamByConfigGroup(String configGroup) {
        if (StringUtils.isEmpty(configGroup)) {
            LOG.warn("Empty config group input.");
            return null;
        }

        HashMap<String, Object> map = new HashMap<>();

        List<SdConfigParamEntity> configEntityList = sdConfigParamMapper.getValuesByConfigGroup(configGroup);
        for (SdConfigParamEntity e : configEntityList) {
            Object obj = getValue(e);
            map.put(e.getConfigKey(), obj);
        }
        return map;
    }

    @Override
    protected BtuConfigParamEntity getConfigParamEntity(String configGroup, String configKey, String configValueType) {
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
}
