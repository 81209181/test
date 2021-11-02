package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.facade.data.SdTicketInfoData;
import com.hkt.btu.sd.facade.data.SdTicketMasData;

public class SdTicketInfoDataPopulator extends AbstractDataPopulator<SdTicketInfoData> {

    public void populateFromSdTicketMasData(SdTicketMasData source, SdTicketInfoData target) {
        target.setCustCode(source.getCustCode());
        target.setTicketMasId(source.getTicketMasId());
        target.setTicketStatus(source.getStatus());
        target.setTicketStatusDesc(source.getStatusDesc());
        target.setTicketType(source.getTicketType());
        target.setCallInCount(source.getCallInCount());
        target.setSearchKeyDesc(source.getSearchKeyDesc());
        target.setSearchValue(source.getSearchValue());
        target.setOwningRole(source.getOwningRole());
        target.setCustName(source.getCustName());
        target.setCompleteDate(source.getCompleteDate());
    }

}
