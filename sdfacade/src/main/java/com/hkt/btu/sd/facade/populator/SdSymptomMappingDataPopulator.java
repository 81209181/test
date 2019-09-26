package com.hkt.btu.sd.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdSymptomMappingBean;
import com.hkt.btu.sd.facade.data.SdSymptomMappingData;

public class SdSymptomMappingDataPopulator extends AbstractDataPopulator<SdSymptomMappingData> {

    public void populate(SdSymptomMappingBean source, SdSymptomMappingData target) {
        target.setServiceTypeCode(source.getServiceTypeCode());
        target.setSymptomCode(source.getSymptomCode());
    }
}
