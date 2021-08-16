package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdGmbTicketEavEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomWorkingPartyMappingEntity;
import com.hkt.btu.sd.core.dao.entity.SdTicketServiceEntity;
import com.hkt.btu.sd.core.service.bean.SdGmbTicketEavBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomWorkingPartyMappingBean;
import com.hkt.btu.sd.core.service.bean.SdTicketServiceBean;

import java.util.Optional;
import java.util.stream.Collectors;

public class SdTicketServiceBeanPopulator extends AbstractBeanPopulator<SdTicketServiceBean> {

    public void populate(SdTicketServiceEntity source, SdTicketServiceBean target) {
        super.populate(source, target);
        target.setTicketDetId(source.getTicketDetId());
        target.setTicketMasId(source.getTicketMasId());
        target.setServiceId(source.getServiceId());
        target.setServiceTypeCode(source.getServiceTypeCode());
        target.setJobId(source.getJobId());
        target.setSubsId(source.getSubsId());
        target.setReportTime(source.getReportTime());
        Optional.ofNullable(source.getSymptomList()).ifPresent(entities -> target.setFaultsList(entities.stream().map(entity -> {
            SdSymptomBean bean = new SdSymptomBean();
            bean.setSymptomCode(entity.getSymptomCode());
            bean.setSymptomDescription(entity.getSymptomDescription());
            bean.setSymptomGroupCode(entity.getSymptomGroupCode());
            bean.setSymptomGroupName(entity.getSymptomGroupName());
            bean.setVoiceLineTest(entity.getVoiceLineTest());
            bean.setApptMode(entity.getApptMode());
            return bean;
        }).collect(Collectors.toList())));
    }


    public void populate(SdSymptomEntity source, SdSymptomBean target) {
        target.setSymptomCode(source.getSymptomCode());
        target.setSymptomDescription(source.getSymptomDescription());
        target.setVoiceLineTest(source.getVoiceLineTest());
        target.setApptMode(source.getApptMode());
    }

    public void populate(SdSymptomWorkingPartyMappingEntity source, SdSymptomWorkingPartyMappingBean target) {
        target.setSymptomCode(source.getSymptomCode());
        target.setServiceTypeCode(source.getServiceTypeCode());
        target.setWorkingParty(source.getWorkingParty());
    }

    public void populate(SdGmbTicketEavEntity source, SdGmbTicketEavBean target) {
        target.setTicketMasId(source.getTicketMasId());
        target.setPsl(source.getPsl());
        target.setRouteNo(source.getRouteNo());
    }
}
