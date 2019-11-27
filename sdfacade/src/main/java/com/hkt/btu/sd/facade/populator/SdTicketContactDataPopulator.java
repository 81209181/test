package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdTicketContactBean;
import com.hkt.btu.sd.facade.data.SdTicketContactData;

public class SdTicketContactDataPopulator extends AbstractDataPopulator<SdTicketContactData> {

    public void populate(SdTicketContactBean source, SdTicketContactData target) {
        target.setTicketMasId(source.getTicketMasId());
        target.setContactType(source.getContactType());
        target.setContactName(source.getContactName());
        target.setContactEmail(source.getContactEmail());
        target.setContactMobile(source.getContactMobile());
        target.setContactNumber(source.getContactNumber());
    }
}
