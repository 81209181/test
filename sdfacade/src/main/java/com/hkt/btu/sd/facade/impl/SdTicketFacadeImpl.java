package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.core.exception.AuthorityNotFoundException;
import com.hkt.btu.sd.core.service.SdTicketService;
import com.hkt.btu.sd.core.service.bean.SdTicketContactBean;
import com.hkt.btu.sd.core.service.bean.SdTicketMasBean;
import com.hkt.btu.sd.facade.SdTicketFacade;
import com.hkt.btu.sd.facade.data.SdTicketContactData;
import com.hkt.btu.sd.facade.data.SdTicketMasData;
import com.hkt.btu.sd.facade.populator.SdTicketContactDataPopulator;
import com.hkt.btu.sd.facade.populator.SdTicketMasDataPopulator;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
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
            ticketService.updateContactInfo(data.getTicketMasId(), data.getContactTypeValue(), data.getContactName(), data.getContactNumber(), data.getContactEmail(), data.getContactMobile());
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

    @Override
    public PageData<SdTicketMasData> searchTicketList(Pageable pageable, String dateFrom, String dateTo, String status) {
        Page<SdTicketMasBean> pageBean;
        try {
            pageBean = ticketService.searchTicketList(pageable, dateFrom, dateTo, status);
        } catch (AuthorityNotFoundException e) {
            return new PageData<>(e.getMessage());
        }

        // populate content
        List<SdTicketMasBean> beanList = pageBean.getContent();
        List<SdTicketMasData> dataList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(beanList)) {
            for (SdTicketMasBean bean : beanList) {
                SdTicketMasData data = new SdTicketMasData();
                ticketMasDataPopulator.populate(bean, data);
                dataList.add(data);
            }
        }

        return new PageData<>(dataList, pageBean.getPageable(), pageBean.getTotalElements());
    }

    @Override
    public List<SdTicketMasData> getMyTicket() {
        List<SdTicketMasBean> beanList = ticketService.getMyTicket();

        // populate content
        List<SdTicketMasData> dataList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(beanList)) {
            for (SdTicketMasBean bean : beanList) {
                SdTicketMasData data = new SdTicketMasData();
                ticketMasDataPopulator.populate(bean, data);
                dataList.add(data);
            }
        }
        return dataList;
    }
}
