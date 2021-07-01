package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdTicketExportEntity;
import com.hkt.btu.sd.core.dao.entity.SdTicketMasEntity;
import com.hkt.btu.sd.core.service.bean.SdTicketExportBean;
import com.hkt.btu.sd.core.service.bean.SdTicketMasBean;
import com.hkt.btu.sd.core.service.constant.TicketStatusEnum;

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
        target.setArrivalDate(source.getArrivalDate());
        target.setCompleteDate(source.getCompleteDate());
        target.setServiceType(source.getServiceType());
        target.setOwningRole(source.getOwningRole());
        target.setServiceNumber(source.getServiceNumber());
        target.setCustName(source.getCustName());

        String statusCode = source.getStatus();
        target.setStatus(TicketStatusEnum.getEnum(statusCode));
    }

    public void populate(SdTicketExportEntity source, SdTicketExportBean target) {
        target.setTicketMasId(source.getTicketMasId());
        target.setTicketType(source.getTicketType());
        target.setCreateBy(source.getCreateBy());
        target.setCreateDate(source.getCreateDate());
        target.setModifyBy(source.getModifyBy());
        target.setModifyDate(source.getModifyDate());
        target.setCallInCount(source.getCallInCount());
        target.setCompleteDate(source.getCompleteDate());
        target.setOwningRole(source.getOwningRole());
        target.setServiceNumber(source.getServiceNumber());
        target.setStatus(source.getStatus());
        target.setReportTime(source.getReportTime());
        target.setSymptomDescription(source.getSymptomDescription());
        target.setSdCloseCodeDescription(source.getSdCloseCodeDescription());
        target.setWfmClearCode(source.getWfmClearCode());
        target.setWfmSubClearCode(source.getWfmSubClearCode());
    }
}
