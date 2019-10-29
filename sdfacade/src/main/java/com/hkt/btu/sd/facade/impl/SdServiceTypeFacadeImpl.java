package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.SdServiceTypeService;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.facade.SdServiceTypeFacade;
import com.hkt.btu.sd.facade.data.SdServiceTypeData;
import com.hkt.btu.sd.facade.populator.SdServiceTypeDataPopulator;
import org.apache.commons.lang3.StringUtils;

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
    public String getServiceTypeDescByServiceTypeCode(String code) {
        return serviceTypeService.getServiceTypeDescByServiceTypeCode(code);
    }

    @Override
    public SdServiceTypeData getServiceTypeByOfferName(String offerName) {
        SdServiceTypeData data = new SdServiceTypeData();
        SdServiceTypeBean bean = serviceTypeService.getServiceTypeByOfferName(offerName);
        serviceTypeDataPopulator.populate(bean,data);
        return data;
    }

    @Override
    public boolean needCheckPendingOrder(String serviceType) {
        if(StringUtils.isEmpty(serviceType)){
            return false;
        }

        switch (serviceType){
            case SdServiceTypeBean.SERVICE_TYPE.BROADBAND:
            case SdServiceTypeBean.SERVICE_TYPE.VOIP:
                return true;
            default:
                return false;
        }
    }


}
