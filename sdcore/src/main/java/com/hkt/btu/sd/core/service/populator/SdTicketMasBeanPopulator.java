package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdTicketMasEntity;
import com.hkt.btu.sd.core.service.bean.SdTicketMasBean;

import static com.hkt.btu.sd.core.service.bean.SdTicketMasBean.*;

public class SdTicketMasBeanPopulator extends AbstractBeanPopulator<SdTicketMasBean> {

    public void populate(SdTicketMasEntity source, SdTicketMasBean target) {
        target.setTicketMasId(source.getTicketMasId());
        target.setCustCode(source.getCustCode());
        target.setTicketType(source.getTicketType());
        target.setCreateby(source.getCreateby());
        target.setCreatedate(source.getCreatedate());
        target.setModifyby(source.getModifyby());
        target.setModifydate(source.getModifydate());
        target.setAppointmentDate(source.getAppointmentDate());
        target.setAsap(source.getAsap());
        target.setCallInCount(source.getCallInCount());
        switch (source.getStatus()) {
            case STATUS_TYPE_CODE.OPEN:
                target.setStatus(STATUS_TYPE.OPEN);
                break;
            case STATUS_TYPE_CODE.CANCEL:
                target.setStatus(STATUS_TYPE.CANCEL);
                break;
            case STATUS_TYPE_CODE.WORKING:
                target.setStatus(STATUS_TYPE.WORKING);
                break;
            case STATUS_TYPE_CODE.COMPLETE:
                target.setStatus(STATUS_TYPE.COMPLETE);
                break;
        }
    }
}
