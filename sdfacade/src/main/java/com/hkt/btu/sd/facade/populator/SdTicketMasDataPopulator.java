package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdTicketMasBean;
import com.hkt.btu.sd.facade.data.SdTicketMasData;

public class SdTicketMasDataPopulator extends AbstractDataPopulator<SdTicketMasData> {

    public void populate(SdTicketMasBean source, SdTicketMasData target) {
        target.setTicketMasId(source.getTicketMasId());
        target.setCustCode(source.getCustCode());
        target.setTicketType(source.getTicketType());
        target.setStatus(source.getStatus());
        target.setCreateBy(source.getCreateby());
        target.setCreateDate(source.getCreatedate());
    }
}
