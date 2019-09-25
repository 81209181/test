package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdSymptomMappingEntity;
import com.hkt.btu.sd.core.service.bean.SdSymptomMappingBean;

public class SdSymptomMappingBeanPopulator extends AbstractBeanPopulator<SdSymptomMappingBean> {

    public void populate(SdSymptomMappingEntity source, SdSymptomMappingBean target) {
        target.setServiceTypeCode(source.getServiceTypeCode());
        target.setSymptomCode(source.getSymptomCode());
    }
}
