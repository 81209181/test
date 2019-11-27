package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdTicketMasBean;
import com.hkt.btu.sd.facade.constant.ServiceSearchEnum;
import com.hkt.btu.sd.facade.data.SdTicketMasData;

public class SdTicketMasDataPopulator extends AbstractDataPopulator<SdTicketMasData> {

    public void populate(SdTicketMasBean source, SdTicketMasData target) {
        target.setTicketMasId(source.getTicketMasId());
        target.setCustCode(source.getCustCode());
        target.setTicketType(source.getTicketType());
        target.setStatus(source.getStatus() == null ? null : source.getStatus().getStatusCode());
        target.setStatusDesc(source.getStatus() == null ? null : source.getStatus().getStatusDesc());
        target.setCreateBy(source.getCreateby());
        target.setCreateDate(source.getCreatedate());
        target.setModifyBy(source.getModifyby());
        target.setModifyDate(source.getModifydate());
        target.setCallInCount(source.getCallInCount());
        target.setSearchKey(source.getSearchKey());
        target.setSearchValue(source.getSearchValue());
        target.setCompleteDate(source.getCompleteDate());
        target.setServiceType(source.getServiceType());
        target.setOwningRole(source.getOwningRole());
        target.setServiceNumber(source.getServiceNumber());

        ServiceSearchEnum serviceSearchEnum = ServiceSearchEnum.getEnum(source.getSearchKey());
        if (serviceSearchEnum != null) {
            target.setSearchKeyDesc(serviceSearchEnum.getKeyDesc());
        }
    }
}
