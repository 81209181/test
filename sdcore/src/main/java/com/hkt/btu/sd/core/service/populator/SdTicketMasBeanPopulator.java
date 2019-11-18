package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdTicketMasEntity;
import com.hkt.btu.sd.core.service.bean.SdTicketMasBean;
import com.hkt.btu.sd.core.service.constant.TicketStatusEnum;
import org.apache.commons.lang3.StringUtils;

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
        target.setSearchKey(source.getSearchKey());
        target.setSearchValue(source.getSearchValue());
        target.setCompleteDate(source.getCompleteDate());
        target.setServiceType(source.getServiceType());
        target.setOwningRole(source.getOwningRole());

        String statusCode = source.getStatus();
        target.setStatus(TicketStatusEnum.getEnum(statusCode));
    }
}
