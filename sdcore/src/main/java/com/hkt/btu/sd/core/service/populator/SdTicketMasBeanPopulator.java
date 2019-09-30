package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdTicketMasEntity;
import com.hkt.btu.sd.core.service.bean.SdTicketMasBean;

public class SdTicketMasBeanPopulator extends AbstractBeanPopulator<SdTicketMasBean> {

    public void populate(SdTicketMasEntity source, SdTicketMasBean target) {
        target.setTicketMasId(source.getTicketMasId());
        target.setCustCode(source.getCustCode());
        target.setTicketType(source.getTicketType());
        target.setStatus(source.getStatus());
        target.setCreateby(source.getCreateby());
        target.setCreatedate(source.getCreatedate());
        target.setAppointmentDate(source.getAppointmentDate());
        target.setAsap(source.getAsap());
    }
}
