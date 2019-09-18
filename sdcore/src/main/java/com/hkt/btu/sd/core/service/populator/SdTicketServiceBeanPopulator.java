package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdTicketServiceEntity;
import com.hkt.btu.sd.core.service.bean.SdServiceFaultsBean;
import com.hkt.btu.sd.core.service.bean.SdTicketServiceBean;

import java.util.stream.Collectors;

public class SdTicketServiceBeanPopulator extends AbstractBeanPopulator<SdTicketServiceBean> {

    public void populate(SdTicketServiceEntity source, SdTicketServiceBean target) {
        super.populate(source, target);
        target.setTicketDetId(source.getTicketDetId());
        target.setTicketMasId(source.getTicketMasId());
        target.setServiceId(source.getServiceId());
        target.setServiceTypeCode(source.getServiceTypeCode());
        target.setFaultsList(source.getFaults().stream().map(entity -> {
            SdServiceFaultsBean bean = new SdServiceFaultsBean();
            bean.setTicketFaultsId(entity.getTicketFaultsId());
            bean.setTicketDetId(entity.getTicketDetId());
            bean.setFaults(entity.getFaults());
            return bean;
        }).collect(Collectors.toList()));
    }
}
