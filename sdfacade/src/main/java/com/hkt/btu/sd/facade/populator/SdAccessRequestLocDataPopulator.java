package com.hkt.btu.sd.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdAccessRequestLocBean;
import com.hkt.btu.sd.facade.data.SdAccessRequestLocData;

public class SdAccessRequestLocDataPopulator extends AbstractDataPopulator<SdAccessRequestLocData> {

    public void populate(SdAccessRequestLocBean source, SdAccessRequestLocData target) {
        target.setLocId(source.getLocId());
        target.setName(source.getName());
    }
}