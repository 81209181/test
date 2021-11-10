package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.exception.BtuMissingImplException;
import com.hkt.btu.common.core.service.BtuConfigParamService;
import com.hkt.btu.common.core.service.BtuSensitiveDataService;
import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;
import com.hkt.btu.common.core.service.constant.BtuConfigParamTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Base64Utils;

import javax.annotation.Resource;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BtuConfigParamServiceImpl implements BtuConfigParamService {
    private static final Logger LOG = LogManager.getLogger(BtuConfigParamServiceImpl.class);

    @Resource(name = "sensitiveDataService")
    private BtuSensitiveDataService sensitiveDataService;

    @Override
    public boolean createConfigParam(String configGroup, String configKey, String configValue, BtuConfigParamTypeEnum configValueType, String encrypt) throws GeneralSecurityException {
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
    }

    @Override
    public boolean updateConfigParam(String configGroup, String configKey, String configValue, BtuConfigParamTypeEnum configValueType, String encrypt) throws GeneralSecurityException {
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
    }

    @Override
    public List<String> getConfigGroupList() {
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
    }

    public List<BtuConfigParamBean> getConfigParamBeanListInternal(String configGroup, String configKey){
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
    }

    @Override
    public List<String> getConfigTypeList() {
        return Stream.of(BtuConfigParamTypeEnum.values())
                .map(BtuConfigParamTypeEnum::getTypeCode)
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkConfigParamInput(String configGroup, String configKey, String configValue, BtuConfigParamTypeEnum configValueType) {
        if(configValueType==null){
            return false;
        }

        switch (configValueType){
            case STRING:
                return true;
            case LOCAL_DATE_TIME:
                try {
                    LocalDateTime.parse(configValue);
                    return true;
                } catch (DateTimeParseException e) {
                    return false;
                }
            case BOOLEAN:
                return StringUtils.equalsIgnoreCase(configValue, Boolean.TRUE.toString()) ||
                        StringUtils.equalsIgnoreCase(configValue, Boolean.FALSE.toString());
            case INTEGER:
                return NumberUtils.isDigits(configValue);
            case DOUBLE:
            if (!NumberUtils.isNumber(configValue)) { // Deprecated in new lib
//                if (!NumberUtils.isCreatable(configValue)) {
                    return false;
                }
                return configValue.contains(".");
            case UNKNOWN:
            default:
                return false;
        }
    }

    @Override
    public boolean checkConfigKey(String configGroup, String configKey) {
        BtuConfigParamBean btuConfigParamBean = getConfigParamByGroupAndKey(configGroup, configKey);
        return btuConfigParamBean!=null;
    }

    protected Object getValue(BtuConfigParamBean configParamBean) {
        if (configParamBean == null) {
            return null;
        }

        BtuConfigParamTypeEnum type = configParamBean.getConfigValueType();
        String value = configParamBean.getConfigValue();

        try {
            switch (type){
                case STRING:
                    return value;
                case INTEGER:
                    return Integer.parseInt(value);
                case DOUBLE:
                    return Double.parseDouble(value);
                case BOOLEAN:
                    return BooleanUtils.toBoolean(value);
                case LOCAL_DATE_TIME:
                    return LocalDateTime.parse(value);
                case UNKNOWN:
                default:
            }
        } catch (DateTimeParseException | NullPointerException | NumberFormatException e) {
            LOG.warn(e.getMessage());
        }

        LOG.warn(String.format("Cannot parse %s to %s.", value, type));
        return null;
    }

    @Override
    public List<BtuConfigParamBean> getAllConfigParam() {
        return getConfigParamBeanListInternal(null, null);
    }

    @Override
    public BtuConfigParamBean getConfigParamByGroupAndKey(String configGroup, String configKey) {
        if(StringUtils.isEmpty(configGroup)){
            LOG.warn("Empty configGroup.");
            return null;
        } else if (StringUtils.isEmpty(configKey)){
            LOG.warn("Empty configKey.");
            return null;
        }

        List<BtuConfigParamBean> beanList = getConfigParamBeanListInternal(configGroup, configKey);
        if (CollectionUtils.isEmpty(beanList)) {
            LOG.info("Found no config param. (configGroup={}, configKey= {})", configGroup, configKey);
            return null;
        }

        // decrypt
        BtuConfigParamBean bean = beanList.get(0);
        decryptConfigValue(bean);
        return bean;
    }

    @Override
    public Map<String, Object> getConfigParamByConfigGroup(String configGroup, boolean decrypt) {
        if (StringUtils.isEmpty(configGroup)) {
            LOG.warn("Empty configGroup.");
            return null;
        }

        List<BtuConfigParamBean> beanList = getConfigParamBeanListInternal(configGroup, null);
        if (CollectionUtils.isEmpty(beanList)) {
            LOG.info("Found no config param. (configGroup={})", configGroup);
            return new HashMap<>();
        }

        HashMap<String, Object> map = new HashMap<>();
        for (BtuConfigParamBean bean : beanList) {
            if (decrypt) {
                decryptConfigValue(bean);
            }
            Object obj = getValue(bean);
            map.put(bean.getConfigKey(), obj);
        }
        return map;
    }

    protected String getEncryptedString(String configValue) throws GeneralSecurityException {
        byte[] encryptBytes = sensitiveDataService.encryptFromString(configValue);
        return Base64Utils.encodeToString(encryptBytes);
    }

    private String getDecryptedString(String configValue) {
        byte[] encryptBytes = Base64Utils.decodeFromString(configValue);
        return sensitiveDataService.decryptToStringSafe(encryptBytes);
    }

    private void decryptConfigValue(BtuConfigParamBean bean){
        if(bean==null){
            return;
        }

        if(bean.isEncrypt()){
            String decryptedValue = getDecryptedString(bean.getConfigValueRaw());
            bean.setConfigValue(decryptedValue);
        }
    }

    private BtuConfigParamBean getConfigParamWithType(String configGroup, String configKey, BtuConfigParamTypeEnum expectConfigValueType) {
        if (StringUtils.isEmpty(configGroup)) {
            LOG.warn("Empty config group input.");
            return null;
        } else if (StringUtils.isEmpty(configKey)) {
            LOG.warn("Empty config key input.");
            return null;
        } else if (expectConfigValueType==null){
            LOG.warn("Empty config value type input.");
            return null;
        }

        BtuConfigParamBean bean = getConfigParamByGroupAndKey(configGroup, configKey);
        if(bean==null){
            return null;
        }

        // check type
        if (expectConfigValueType != bean.getConfigValueType()) {
            LOG.warn("ConfigParam value type should be " + expectConfigValueType +
                    " instead of " + bean.getConfigValueType() + ".");
            return null;
        }
        return bean;
    }

    @Override
    public String getString(String configGroup, String configKey) {
        BtuConfigParamBean bean = getConfigParamWithType(configGroup, configKey, BtuConfigParamTypeEnum.STRING);
        return (String) getValue(bean);
    }

    @Override
    public Integer getInteger(String configGroup, String configKey) {
        BtuConfigParamBean bean = getConfigParamWithType(configGroup, configKey, BtuConfigParamTypeEnum.INTEGER);
        return (Integer) getValue(bean);
    }

    @Override
    public Double getDouble(String configGroup, String configKey) {
        BtuConfigParamBean bean = getConfigParamWithType(configGroup, configKey, BtuConfigParamTypeEnum.DOUBLE);
        return (Double) getValue(bean);
    }

    @Override
    public Boolean getBoolean(String configGroup, String configKey) {
        BtuConfigParamBean bean = getConfigParamWithType(configGroup, configKey, BtuConfigParamTypeEnum.BOOLEAN);
        return (Boolean) getValue(bean);
    }

    @Override
    public LocalDateTime getDateTime(String configGroup, String configKey) {
        BtuConfigParamBean bean = getConfigParamWithType(configGroup, configKey, BtuConfigParamTypeEnum.LOCAL_DATE_TIME);
        return (LocalDateTime) getValue(bean);
    }
}
