package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.SdConfigParamService;
import com.hkt.btu.sd.core.service.bean.SdConfigParamBean;
import com.hkt.btu.sd.facade.SdConfigParamFacade;
import com.hkt.btu.sd.facade.data.SdConfigParamData;
import com.hkt.btu.sd.facade.populator.SdConfigParamDataPopulator;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


public class SdConfigParamFacadeImpl implements SdConfigParamFacade {

    @Resource(name = "configParamService")
    SdConfigParamService sdConfigParamService;

    @Resource(name = "configParamDataPopulator")
    SdConfigParamDataPopulator sdConfigParamDataPopulator;

    @Override
    public List<SdConfigParamData> getAllConfigParam() {
        List<SdConfigParamBean> beanList = sdConfigParamService.getAllConfigParam();
        if (CollectionUtils.isEmpty(beanList)) {
            return null;
        }

        List<SdConfigParamData> dataList = new LinkedList<>();
        for (SdConfigParamBean bean : beanList) {
            SdConfigParamData data = new SdConfigParamData();
            sdConfigParamDataPopulator.populate(bean, data);
            dataList.add(data);
        }

        return dataList;
    }

    @Override
    public Optional<SdConfigParamData> getConfigParamByGroupAndKey(String configGroup, String configKey) {
        Optional<SdConfigParamBean> bean = sdConfigParamService.getConfigParamByGroupAndKey(configGroup, configKey);
        if (bean.isEmpty()) {
            return Optional.empty();
        }
        SdConfigParamData data = new SdConfigParamData();
        sdConfigParamDataPopulator.populate(bean.get(), data);
        return Optional.of(data);
    }

    @Override
    public List<String> getConfigTypeList() {
        return sdConfigParamService.getConfigTypeList();
    }

    @Override
    public boolean updateConfigParam(String configGroup, String configKey, String configValue, String configValueType) {
        return sdConfigParamService.updateConfigParam(configGroup,configKey,configValue,configValueType);
    }

    @Override
    public List<String> getConfigGroupList() {
        return sdConfigParamService.getConfigGroupList();
    }

    @Override
    public boolean createConfigParam(String configGroup, String configKey, String configValue, String configValueType) {
        return sdConfigParamService.createConfigParam(configGroup,configKey,configValue,configValueType);
    }

    @Override
    public boolean checkConfigKey(String configGroup, String configKey) {
        return sdConfigParamService.checkConfigKey(configGroup,configKey);
    }
}
