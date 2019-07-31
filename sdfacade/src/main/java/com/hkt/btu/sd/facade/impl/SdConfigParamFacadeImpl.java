package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.SdConfigParamService;
import com.hkt.btu.sd.core.service.bean.SdConfigParamBean;
import com.hkt.btu.sd.facade.SdConfigParamFacade;
import com.hkt.btu.sd.facade.data.SdConfigParamData;
import com.hkt.btu.sd.facade.populator.SdConfigParamDataPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

public class SdConfigParamFacadeImpl implements SdConfigParamFacade {

    @Resource(name = "configParamService")
    SdConfigParamService sdConfigParamService;

    @Resource(name = "configParamDataPopulator")
    SdConfigParamDataPopulator sdConfigParamDataPopulator;

    @Override
    public List<SdConfigParamData> getAllConfigParam() {
        List<SdConfigParamBean> beanList = sdConfigParamService.getAllConfigParam();
        if(CollectionUtils.isEmpty(beanList)){
            return null;
        }

        List<SdConfigParamData> dataList = new LinkedList<>();
        for(SdConfigParamBean bean : beanList){
            SdConfigParamData data = new SdConfigParamData();
            sdConfigParamDataPopulator.populate(bean, data);
            dataList.add(data);
        }

        return dataList;
    }
}
