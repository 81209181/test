package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdTicketRemarkEntity;
import com.hkt.btu.sd.core.service.bean.SdTicketRemarkBean;

public class SdTicketRemarkBeanPopulator extends AbstractBeanPopulator<SdTicketRemarkBean> {

    public void populate(SdTicketRemarkEntity source, SdTicketRemarkBean target) {
        target.setTicketMasId(source.getTicketMasId());
        target.setRemarks(source.getRemarks());
        target.setCreatedate(source.getCreatedate());
        target.setCreateby(source.getCreateby());

        switch (source.getRemarksType()) {
            case SdTicketRemarkEntity.REMARKS_TYPE.CUSTOMER:
                target.setRemarksType(SdTicketRemarkBean.REMARKS_TYPE.CUSTOMER);
                break;
            case SdTicketRemarkEntity.REMARKS_TYPE.FIELD:
                target.setRemarksType(SdTicketRemarkBean.REMARKS_TYPE.FIELD);
                break;
            case SdTicketRemarkEntity.REMARKS_TYPE.SYSTEM:
                target.setRemarksType(SdTicketRemarkBean.REMARKS_TYPE.SYSTEM);
                break;
            default:
                target.setRemarksType(source.getRemarksType());
        }
    }
}
