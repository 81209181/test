package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.service.BtuSensitiveDataService;
import com.hkt.btu.common.core.service.impl.BtuConfigParamServiceImpl;
import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.dao.mapper.SdConfigParamMapper;
import com.hkt.btu.sd.core.service.SdConfigParamService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdConfigParamBean;
import com.hkt.btu.sd.core.service.populator.SdConfigParamBeanPopulator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class SdConfigParamServiceImpl extends BtuConfigParamServiceImpl implements SdConfigParamService {
    private static final Logger LOG = LogManager.getLogger(SdConfigParamServiceImpl.class);

    @Resource
    private SdConfigParamMapper sdConfigParamMapper;

    @Resource(name = "configParamBeanPopulator")
    SdConfigParamBeanPopulator sdConfigParamBeanPopulator;
    @Resource(name = "userService")
    SdUserService sdUserService;
    @Resource(name = "btuSensitiveDataService")
    BtuSensitiveDataService btuSensitiveDataService;

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
        } else if (!StringUtils.equals(configValueType, configEntity.getConfigValueType())) {
            LOG.warn("ConfigParam value type should be " + configValueType +
                    " instead of " + configEntity.getConfigValueType() + ".");
            return null;
        }

        return configEntity;
    }

    @Override
    public List<SdConfigParamBean> getAllConfigParam() {
        List<SdConfigParamEntity> entityList = sdConfigParamMapper.getAllConfigParam();
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        List<SdConfigParamBean> beanList = new LinkedList<>();
        for (SdConfigParamEntity entity : entityList) {
            if (StringUtils.isNotEmpty(entity.getEncrypt()) && entity.getEncrypt().equals("Y")) {
                entity.setConfigValue("ENCRYPTED");
            }
            SdConfigParamBean bean = new SdConfigParamBean();
            sdConfigParamBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }

        return beanList;
    }

    @Override
    public Optional<SdConfigParamBean> getConfigParamByGroupAndKey(String configGroup, String configKey) {
        SdConfigParamEntity entity = sdConfigParamMapper.getValue(configGroup, configKey);
        if (entity == null) {
            return Optional.empty();
        }
        if (StringUtils.isNotEmpty(entity.getEncrypt()) && entity.getEncrypt().equals("Y")) {
            String decryptStr = btuSensitiveDataService.decryptToStringSafe(Base64Utils.decodeFromString(entity.getConfigValue()));
            entity.setConfigValue(decryptStr);
        }
        SdConfigParamBean bean = new SdConfigParamBean();
        sdConfigParamBeanPopulator.populate(entity, bean);
        return Optional.of(bean);
    }

    @Override
    public boolean updateConfigParam(String configGroup, String configKey, String configValue, String configValueType, String encrypt) throws GeneralSecurityException {
        if (StringUtils.isEmpty(encrypt)) {
            encrypt = "N";
        } else {
            byte[] encryptBytes = btuSensitiveDataService.encryptFromString(configValue);
            configValue = Base64Utils.encodeToString(encryptBytes);
            encrypt = "Y";
        }
        return sdConfigParamMapper.updateValue(configGroup, configKey, configValue, configValueType, sdUserService.getCurrentUserUserId(), encrypt) > 0;
    }

    @Override
    public List<String> getConfigGroupList() {
        return sdConfigParamMapper.getConfigGroupList();
    }

    @Override
    public boolean createConfigParam(String configGroup, String configKey, String configValue, String configValueType, String encrypt) throws GeneralSecurityException {
        if (StringUtils.isEmpty(encrypt)) {
            encrypt = "N";
        } else {
            byte[] encryptBytes = btuSensitiveDataService.encryptFromString(configValue);
            configValue = Base64Utils.encodeToString(encryptBytes);
            encrypt = "Y";
        }
        return sdConfigParamMapper.insertConfig(configGroup, configKey, configValue, configValueType, sdUserService.getCurrentUserUserId(), encrypt);
    }

    @Override
    public boolean checkConfigKey(String configGroup, String configKey) {
        Optional<SdConfigParamEntity> entity = Optional.ofNullable(sdConfigParamMapper.getValue(configGroup, configKey));
        if (entity.isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkConfigParam(String configGroup, String configKey, String configValue, String configValueType) {
        if (BtuConfigParamEntity.TYPE.BOOLEAN.equals(configValueType)) {
            return configValue.equalsIgnoreCase("true") || configValue.equalsIgnoreCase("false");
        } else if (BtuConfigParamEntity.TYPE.LOCAL_DATE_TIME.equals(configValueType)) {
            try {
                LocalDateTime.parse(configValue);
            } catch (DateTimeParseException e) {
                return false;
            }
        } else if (BtuConfigParamEntity.TYPE.INTEGER.equals(configValueType)) {
            return NumberUtils.isDigits(configValue);
        } else if (BtuConfigParamEntity.TYPE.DOUBLE.equals(configValueType)) {
            if (!NumberUtils.isNumber(configValue)) {
                return false;
            }
            return configValue.contains(".");

        }
        return true;
    }
}
