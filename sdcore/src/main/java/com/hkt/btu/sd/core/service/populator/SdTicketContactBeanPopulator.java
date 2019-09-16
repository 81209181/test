package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdTicketContactEntity;
import com.hkt.btu.sd.core.service.bean.SdTicketContactBean;

public class SdTicketContactBeanPopulator extends AbstractBeanPopulator<SdTicketContactBean> {

    public void populate(SdTicketContactEntity source, SdTicketContactBean target) {
        target.setContactType(source.getContactType());
        target.setContactName(source.getContactName());
        target.setContactEmail(source.getContactEmail());
        target.setContactNumber(source.getContactNumber());
        target.setContactMobile(source.getContactMobile());
    }
}
