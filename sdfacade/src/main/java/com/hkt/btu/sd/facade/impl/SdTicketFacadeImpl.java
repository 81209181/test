package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.SdTicketService;
import com.hkt.btu.sd.core.service.bean.SdTicketMasBean;
import com.hkt.btu.sd.facade.SdTicketFacade;
import com.hkt.btu.sd.facade.data.SdTicketMasData;
import com.hkt.btu.sd.facade.populator.SdTicketMasDataPopulator;

import javax.annotation.Resource;
import java.util.Optional;

public class SdTicketFacadeImpl implements SdTicketFacade {

    @Resource(name = "ticketService")
    SdTicketService ticketService;
    @Resource(name = "ticketMasDataPopulator")
    SdTicketMasDataPopulator ticketMasDataPopulator;

    @Override
    public Optional<SdTicketMasData> createQueryTicket(String custCode) {
        Optional<SdTicketMasBean> bean =ticketService.createQueryTicket(custCode);
        if (bean.isPresent()) {
            SdTicketMasData ticketMasData = new SdTicketMasData();
            ticketMasDataPopulator.populate(bean.get(),ticketMasData);
            return Optional.of(ticketMasData);
        }
        return Optional.empty();
    }

    @Override
    public Optional<SdTicketMasData> getTicket(Integer ticketId) {
        Optional<SdTicketMasBean> bean =ticketService.getTicket(ticketId);
        if (bean.isPresent()) {
            SdTicketMasData ticketMasData = new SdTicketMasData();
            ticketMasDataPopulator.populate(bean.get(),ticketMasData);
            return Optional.of(ticketMasData);
        }
        return Optional.empty();
    }
}
