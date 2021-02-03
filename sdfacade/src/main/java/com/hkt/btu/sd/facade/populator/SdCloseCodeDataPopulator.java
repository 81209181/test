package com.hkt.btu.sd.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdCloseCodeBean;
import com.hkt.btu.sd.facade.data.SdCloseCodeData;

public class SdCloseCodeDataPopulator extends AbstractDataPopulator<SdCloseCodeData> {

    public void populate(SdCloseCodeBean source, SdCloseCodeData target) {
        target.setCloseCode(source.getCloseCode());
        target.setCloseCodeDescription(source.getCloseCodeDescription());
    }
}
