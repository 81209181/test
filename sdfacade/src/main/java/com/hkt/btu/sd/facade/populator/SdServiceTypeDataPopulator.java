package com.hkt.btu.sd.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.facade.data.SdServiceTypeData;

public class SdServiceTypeDataPopulator extends AbstractDataPopulator<SdServiceTypeData> {

    public void populate(SdServiceTypeBean source, SdServiceTypeData target) {
        target.setServiceTypeCode(source.getServiceTypeCode());
        target.setServiceTypeName(source.getServiceTypeName());
    }
}
