package com.hkt.btu.noc.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.noc.core.service.bean.NocAccessRequestLocBean;
import com.hkt.btu.noc.facade.data.NocAccessRequestLocData;

public class NocAccessRequestLocDataPopulator extends AbstractDataPopulator<NocAccessRequestLocData> {

    public void populate(NocAccessRequestLocBean source, NocAccessRequestLocData target) {
        target.setLocId(source.getLocId());
        target.setName(source.getName());
    }
}