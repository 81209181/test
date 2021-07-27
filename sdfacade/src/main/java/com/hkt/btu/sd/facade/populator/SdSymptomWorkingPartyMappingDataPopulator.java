package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdSymptomWorkingPartyMappingBean;
import com.hkt.btu.sd.facade.data.SdSymptomWorkingPartyMappingData;

public class SdSymptomWorkingPartyMappingDataPopulator extends AbstractDataPopulator<SdSymptomWorkingPartyMappingData> {
    public void populate(SdSymptomWorkingPartyMappingBean source, SdSymptomWorkingPartyMappingData target){
        target.setSymptomCode(source.getSymptomCode());
        target.setWorkingParty(source.getWorkingParty());
        target.setServiceTypeCode(source.getServiceTypeCode());
    }

}
