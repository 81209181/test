package com.hkt.btu.noc.facade.impl;

import com.hkt.btu.noc.core.service.NocConfigParamService;
import com.hkt.btu.noc.core.service.bean.NocConfigParamBean;
import com.hkt.btu.noc.facade.NocConfigParamFacade;
import com.hkt.btu.noc.facade.data.NocConfigParamData;
import com.hkt.btu.noc.facade.populator.NocConfigParamDataPopulator;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;


public class NocConfigParamFacadeImpl implements NocConfigParamFacade {

    @Resource(name = "configParamService")
    NocConfigParamService nocConfigParamService;

    @Resource(name = "configParamDataPopulator")
    NocConfigParamDataPopulator nocConfigParamDataPopulator;

    @Override
    public List<NocConfigParamData> getAllConfigParam() {
        List<NocConfigParamBean> beanList = nocConfigParamService.getAllConfigParam();
        if(CollectionUtils.isEmpty(beanList)){
            return null;
        }

        List<NocConfigParamData> dataList = new LinkedList<>();
        for(NocConfigParamBean bean : beanList){
            NocConfigParamData data = new NocConfigParamData();
            nocConfigParamDataPopulator.populate(bean, data);
            dataList.add(data);
        }

        return dataList;
    }
}
