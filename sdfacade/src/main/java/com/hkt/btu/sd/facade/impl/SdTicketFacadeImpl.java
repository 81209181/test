package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.SdTicketService;
import com.hkt.btu.sd.core.service.bean.SdTicketContactBean;
import com.hkt.btu.sd.core.service.bean.SdTicketMasBean;
import com.hkt.btu.sd.facade.SdTicketFacade;
import com.hkt.btu.sd.facade.data.SdTicketContactData;
import com.hkt.btu.sd.facade.data.SdTicketMasData;
import com.hkt.btu.sd.facade.populator.SdTicketContactDataPopulator;
import com.hkt.btu.sd.facade.populator.SdTicketMasDataPopulator;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SdTicketFacadeImpl implements SdTicketFacade {

    @Resource(name = "ticketService")
    SdTicketService ticketService;

    @Resource(name = "ticketMasDataPopulator")
    SdTicketMasDataPopulator ticketMasDataPopulator;
    @Resource(name = "ticketContactDataPopulator")
    SdTicketContactDataPopulator ticketContactDataPopulator;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateContactInfo(List<SdTicketContactData> contactList) {
        ticketService.removeContactInfoByTicketMasId(contactList.get(0).getTicketMasId());
        contactList.forEach(data -> {
            ticketService.updateContactInfo(data.getTicketMasId(), data.getContactType(), data.getContactName(), data.getContactNumber(), data.getContactEmail(), data.getContactMobile());
        });
    }

    @Override
    public List<SdTicketContactData> getContactInfo(Integer ticketMasId) {
        List<SdTicketContactBean> beans=ticketService.getContactInfo(ticketMasId);
        List<SdTicketContactData> dataList = new ArrayList<>();
        beans.forEach(sdTicketContactBean -> {
            SdTicketContactData data = new SdTicketContactData();
            ticketContactDataPopulator.populate(sdTicketContactBean,data);
            dataList.add(data);
        });
        return dataList;
    }
}
