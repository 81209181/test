package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdTicketRemarkBean;
import com.hkt.btu.sd.facade.data.SdTicketRemarkData;

public class SdTicketRemarkDataPopulator extends AbstractDataPopulator<SdTicketRemarkData> {

    public void populate(SdTicketRemarkBean source, SdTicketRemarkData target) {
        target.setTicketMasId(source.getTicketMasId());
        target.setRemarksType(source.getRemarksType());
        target.setRemarks(source.getRemarks());
        target.setCreatedate(source.getCreatedate());
    }
}
