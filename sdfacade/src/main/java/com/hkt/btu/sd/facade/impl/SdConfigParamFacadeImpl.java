package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.service.BtuConfigParamService;
import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;
import com.hkt.btu.sd.facade.SdConfigParamFacade;
import com.hkt.btu.sd.facade.data.SdConfigParamData;
import com.hkt.btu.sd.facade.populator.SdConfigParamDataPopulator;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

public class SdConfigParamFacadeImpl implements SdConfigParamFacade {

    @Resource(name = "configParamService")
    BtuConfigParamService configParamService;

    @Resource(name = "configParamDataPopulator")
    SdConfigParamDataPopulator configParamDataPopulator;

    @Override
    public List<SdConfigParamData> getAllConfigParam() {
        List<BtuConfigParamBean> beanList = configParamService.getAllConfigParam();
        if(CollectionUtils.isEmpty(beanList)){
            return null;
        }

        List<SdConfigParamData> dataList = new LinkedList<>();
        for(BtuConfigParamBean bean : beanList){
            SdConfigParamData data = new SdConfigParamData();
            configParamDataPopulator.populate(bean, data);
            dataList.add(data);
        }

        return dataList;
    }
}
