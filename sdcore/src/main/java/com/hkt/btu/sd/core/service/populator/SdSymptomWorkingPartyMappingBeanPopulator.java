package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdSymptomWorkingPartyMappingEntity;
import com.hkt.btu.sd.core.service.bean.SdSymptomWorkingPartyMappingBean;

public class SdSymptomWorkingPartyMappingBeanPopulator extends AbstractBeanPopulator<SdSymptomWorkingPartyMappingBean> {
    public void populate(SdSymptomWorkingPartyMappingEntity source, SdSymptomWorkingPartyMappingBean target) {
        target.setSymptomCode(source.getSymptomCode());
        target.setWorkingParty(source.getWorkingParty());
        target.setServiceTypeCode(source.getServiceTypeCode());
        target.setSymptomDesc(source.getSymptomDesc());
    }
}