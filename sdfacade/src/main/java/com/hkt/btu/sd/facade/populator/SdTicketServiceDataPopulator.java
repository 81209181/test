package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdTicketServiceBean;
import com.hkt.btu.sd.facade.data.SdServiceFaultsData;
import com.hkt.btu.sd.facade.data.SdTicketServiceData;

import java.util.Optional;
import java.util.stream.Collectors;

public class SdTicketServiceDataPopulator extends AbstractDataPopulator<SdTicketServiceData> {

    public void populate(SdTicketServiceBean source, SdTicketServiceData target) {
        super.populate(source, target);
        target.setTicketDetId(source.getTicketDetId());
        target.setServiceType(source.getServiceTypeCode());
        target.setServiceCode(source.getServiceId());
        target.setJobId(source.getJobId());
        Optional.ofNullable(source.getFaultsList()).ifPresent(beans -> target.setFaultsList(beans.stream().map(bean -> {
            SdServiceFaultsData data = new SdServiceFaultsData();
            data.setFaults(bean.getFaults());
            return data;
        }).collect(Collectors.toList())));
    }
}