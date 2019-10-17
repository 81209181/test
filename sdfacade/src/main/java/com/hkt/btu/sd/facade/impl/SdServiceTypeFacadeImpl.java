package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.SdServiceTypeService;
import com.hkt.btu.sd.facade.SdServiceTypeFacade;
import com.hkt.btu.sd.facade.data.SdServiceTypeData;
import com.hkt.btu.sd.facade.populator.SdServiceTypeDataPopulator;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

public class SdServiceTypeFacadeImpl implements SdServiceTypeFacade {

    @Resource(name = "serviceTypeService")
    SdServiceTypeService serviceTypeService;

    @Resource(name = "serviceTypeDataPopulator")
    SdServiceTypeDataPopulator serviceTypeDataPopulator;

    @Override
    public List<SdServiceTypeData> getServiceTypeList() {
        return serviceTypeService.getServiceTypeList().stream().map(bean -> {
            SdServiceTypeData data = new SdServiceTypeData();
            serviceTypeDataPopulator.populate(bean, data);
            return data;
        }).collect(Collectors.toList());
    }

    @Override
    public String getServiceTypeByOfferName(String offerName) {
        return serviceTypeService.getServiceTypeByOfferName(offerName);
    }
}
