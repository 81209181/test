package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdOutstandingFaultBean;
import com.hkt.btu.sd.facade.data.SdOutstandingFaultData;

public class SdOutstandingFaultDataPopulator extends AbstractDataPopulator<SdOutstandingFaultData> {
    public void populate(SdOutstandingFaultBean source, SdOutstandingFaultData target){
        target.setCount(source.getCount());
        target.setServiceTypeCode(source.getServiceTypeCode());
    }
}
