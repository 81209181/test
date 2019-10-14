package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdTicketRemarkEntity;
import com.hkt.btu.sd.core.service.bean.SdTicketRemarkBean;

public class SdTicketRemarkBeanPopulator extends AbstractBeanPopulator<SdTicketRemarkBean> {

    public void populate(SdTicketRemarkEntity source, SdTicketRemarkBean target) {
        target.setTicketMasId(source.getTicketMasId());
        target.setRemarksType(source.getRemarksTypeDesc());
        target.setRemarks(source.getRemarks());
        target.setCreatedate(source.getCreatedate());
        target.setCreateby(source.getCreateby());
    }
}
