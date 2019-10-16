package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdServiceTypeOfferMappingEntity;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeOfferMappingBean;

public class SdServiceTypeOfferMappingBeanPopulator extends AbstractBeanPopulator<SdServiceTypeOfferMappingBean> {

    public void populate(SdServiceTypeOfferMappingEntity source, SdServiceTypeOfferMappingBean target) {
        target.setServiceTypeCode(source.getServiceTypeCode());
        target.setOfferName(source.getOfferName());
    }
}
