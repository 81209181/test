package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;
import com.hkt.btu.common.core.service.constant.BtuConfigParamTypeEnum;
import com.hkt.btu.common.core.service.impl.BtuConfigParamServiceImpl;
import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.dao.mapper.SdConfigParamMapper;
import com.hkt.btu.sd.core.service.SdConfigParamService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.populator.SdConfigParamBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.security.GeneralSecurityException;
import java.util.LinkedList;
import java.util.List;


public class SdConfigParamServiceImpl extends BtuConfigParamServiceImpl implements SdConfigParamService {
    private static final Logger LOG = LogManager.getLogger(SdConfigParamServiceImpl.class);

    @Resource
    private SdConfigParamMapper sdConfigParamMapper;

    @Resource(name = "configParamBeanPopulator")
    SdConfigParamBeanPopulator sdConfigParamBeanPopulator;
    @Resource(name = "userService")
    SdUserService sdUserService;

    @Override
    public boolean createConfigParam(String configGroup, String configKey, String configValue, BtuConfigParamTypeEnum configValueType, String encrypt) throws GeneralSecurityException {
        if(configValueType==null){
            return false;
        }

        if (StringUtils.isEmpty(encrypt)) {
            encrypt = BtuConfigParamEntity.ENCRYPT.NO;
        } else {
            encrypt = BtuConfigParamEntity.ENCRYPT.YES;
            configValue = getEncryptedString(configValue);
        }

        boolean succeed = sdConfigParamMapper.insertConfig(configGroup, configKey, configValue, configValueType.getTypeCode(), sdUserService.getCurrentUserUserId(), encrypt);
        LOG.info("Inserted new config param. (configGroup={}, configKey={})", configGroup, configKey);
        return succeed;
    }

    @Override
    public boolean updateConfigParam(String configGroup, String configKey, String configValue, BtuConfigParamTypeEnum configValueType, String encrypt) throws GeneralSecurityException {
        if (StringUtils.isEmpty(encrypt)) {
            encrypt = BtuConfigParamEntity.ENCRYPT.NO;
        } else {
            encrypt = BtuConfigParamEntity.ENCRYPT.YES;
            configValue = getEncryptedString(configValue);
        }

        int updatedRows = sdConfigParamMapper.updateValue(configGroup, configKey, configValue, configValueType.getTypeCode(), sdUserService.getCurrentUserUserId(), encrypt);
        boolean succeed = (updatedRows > 0);
        LOG.info("Inserted new config param. (configGroup={}, configKey={})", configGroup, configKey);
        return succeed;
    }

    @Override
    public List<String> getConfigGroupList() {
        return sdConfigParamMapper.getConfigGroupList();
    }

    @Override
    protected List<BtuConfigParamBean> getConfigParamBeanListInternal(String configGroup, String configKey){
        List<SdConfigParamEntity> entityList = sdConfigParamMapper.getConfigParamByGroupAndKey(configGroup, configKey);
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        List<BtuConfigParamBean> beanList = new LinkedList<>();
        for (SdConfigParamEntity entity : entityList) {
            BtuConfigParamBean bean = new BtuConfigParamBean();
            sdConfigParamBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }
        return beanList;
    }
}
