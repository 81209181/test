package com.hkt.btu.sd.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdCompanyBean;
import com.hkt.btu.sd.facade.data.SdCompanyData;

public class SdCompanyDataPopulator extends AbstractDataPopulator<SdCompanyData> {

    public void populate(SdCompanyBean source, SdCompanyData target) {
        target.setCompanyId(source.getCompanyId());
        target.setName( source.getName() );
        target.setRemarks( source.getRemarks() );
    }
}
