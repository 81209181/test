package com.hkt.btu.common.facade.impl;

import com.hkt.btu.common.core.service.BtuConfigParamService;
import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;
import com.hkt.btu.common.core.service.constant.BtuConfigParamTypeEnum;
import com.hkt.btu.common.facade.BtuConfigParamFacade;
import com.hkt.btu.common.facade.data.BtuConfigParamData;
import com.hkt.btu.common.facade.populator.BtuConfigParamDataPopulator;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.security.GeneralSecurityException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BtuConfigParamFacadeImpl implements BtuConfigParamFacade {
    @Resource(name = "configParamService")
    BtuConfigParamService btuConfigParamService;

    @Resource(name = "configParamDataPopulator")
    BtuConfigParamDataPopulator btuConfigParamDataPopulator;

    @Override
    public List<BtuConfigParamData> getAllConfigParam() {
        List<BtuConfigParamBean> beanList = btuConfigParamService.getAllConfigParam();
        if (CollectionUtils.isEmpty(beanList)) {
            return null;
        }

        List<BtuConfigParamData> dataList = new LinkedList<>();
        for (BtuConfigParamBean bean : beanList) {
            BtuConfigParamData data = new BtuConfigParamData();
            btuConfigParamDataPopulator.populate(bean, data);
            dataList.add(data);
        }

        return dataList;
    }

    @Override
    public Optional<BtuConfigParamData> getConfigParamByGroupAndKey(String configGroup, String configKey) {
        BtuConfigParamBean bean = btuConfigParamService.getConfigParamByGroupAndKey(configGroup, configKey);
        if (bean==null) {
            return Optional.empty();
        }
        BtuConfigParamData data = new BtuConfigParamData();
        btuConfigParamDataPopulator.populate(bean, data);
        return Optional.of(data);
    }

    @Override
    public List<String> getConfigTypeList() {
        return btuConfigParamService.getConfigTypeList();
    }

    @Override
    public boolean updateConfigParam(String configGroup, String configKey, String configValue, String configValueType, String encrypt) throws GeneralSecurityException {
        BtuConfigParamTypeEnum configParamTypeEnum = BtuConfigParamTypeEnum.getEnum(configValueType);
        return btuConfigParamService.updateConfigParam(configGroup, configKey, configValue, configParamTypeEnum, encrypt);
    }

    @Override
    public List<String> getConfigGroupList() {
        return btuConfigParamService.getConfigGroupList();
    }

    @Override
    public boolean createConfigParam(String configGroup, String configKey, String configValue, String configValueType, String encrypt) throws GeneralSecurityException {
        BtuConfigParamTypeEnum configParamTypeEnum = BtuConfigParamTypeEnum.getEnum(configValueType);
        return btuConfigParamService.createConfigParam(configGroup, configKey, configValue, configParamTypeEnum, encrypt);
    }

    @Override
    public boolean checkConfigKey(String configGroup, String configKey) {
        return btuConfigParamService.checkConfigKey(configGroup,configKey);
    }

    @Override
    public boolean checkConfigParam(String configGroup, String configKey, String configValue, String configValueType) {
        BtuConfigParamTypeEnum configParamTypeEnum = BtuConfigParamTypeEnum.getEnum(configValueType);
        return btuConfigParamService.checkConfigParamInput(configGroup, configKey, configValue, configParamTypeEnum);
    }
}
