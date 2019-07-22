package com.hkt.btu.noc.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.noc.core.service.bean.NocCompanyBean;
import com.hkt.btu.noc.facade.data.NocCompanyData;

public class NocCompanyDataPopulator extends AbstractDataPopulator<NocCompanyData> {

    public void populate(NocCompanyBean source, NocCompanyData target) {
        target.setCompanyId(source.getCompanyId());
        target.setName( source.getName() );
        target.setRemarks( source.getRemarks() );
    }
}
