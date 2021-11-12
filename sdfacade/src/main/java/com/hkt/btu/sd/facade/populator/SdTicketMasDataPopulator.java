package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdTicketExportBean;
import com.hkt.btu.sd.core.service.bean.SdTicketMasBean;
import com.hkt.btu.sd.core.service.constant.TicketTypeEnum;
import com.hkt.btu.sd.facade.constant.ServiceSearchEnum;
import com.hkt.btu.sd.facade.data.SdTicketExportData;
import com.hkt.btu.sd.facade.data.SdTicketMasData;

public class SdTicketMasDataPopulator extends AbstractDataPopulator<SdTicketMasData> {

    public void populate(SdTicketMasBean source, SdTicketMasData target) {
        target.setTicketMasId(source.getTicketMasId());
//        target.setCustCode(source.getCustCode());
        target.setTicketType(source.getTicketType().getTypeDesc());
        target.setStatus(source.getStatus() == null ? null : source.getStatus().getStatusCode());
        target.setStatusDesc(source.getStatus() == null ? null : source.getStatus().getStatusDesc());
        target.setCreateBy(source.getCreateby());
        target.setCreateDate(source.getCreatedate());
        target.setModifyBy(source.getModifyby());
        target.setModifyDate(source.getModifydate());
//        target.setCallInCount(source.getCallInCount());
//        target.setSearchKey(source.getSearchKey());
//        target.setSearchValue(source.getSearchValue());
//        target.setArrivalDate(source.getArrivalDate());
//        target.setCompleteDate(source.getCompleteDate());
//        target.setServiceType(source.getServiceType());
//        target.setOwningRole(source.getOwningRole());
        target.setServiceNumber(source.getServiceNumber());
//        target.setCustName(source.getCustName());

//        ServiceSearchEnum serviceSearchEnum = ServiceSearchEnum.getEnum(source.getSearchKey());
//        if (serviceSearchEnum != null) {
//            target.setSearchKeyDesc(serviceSearchEnum.getKeyDesc());
//        }
        target.setPriority(source.getPriority().getPriorityDesc());
    }

    public void populate(SdTicketExportBean source, SdTicketExportData target) {
        target.setTicketMasId(source.getTicketMasId());
        target.setTicketType(source.getTicketType());
        target.setCreateBy(source.getCreateBy());
        target.setCreateDate(source.getCreateDate());
        target.setModifyBy(source.getModifyBy());
        target.setModifyDate(source.getModifyDate());
        target.setCompleteDate(source.getCompleteDate());
        target.setServiceNumber(source.getServiceNumber());
        target.setStatus(source.getStatus());
        target.setPriority(source.getPriority());
    }
}
