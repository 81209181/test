package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdTicketMasEntity;
import com.hkt.btu.sd.core.dao.mapper.SdTicketContactMapper;
import com.hkt.btu.sd.core.dao.mapper.SdTicketMasMapper;
import com.hkt.btu.sd.core.service.SdTicketService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdTicketContactBean;
import com.hkt.btu.sd.core.service.bean.SdTicketMasBean;
import com.hkt.btu.sd.core.service.populator.SdTicketContactBeanPopulator;
import com.hkt.btu.sd.core.service.populator.SdTicketMasBeanPopulator;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SdTicketServiceImpl implements SdTicketService {

    @Resource
    SdTicketMasMapper ticketMasMapper;

    @Resource
    SdTicketContactMapper ticketContactMapper;

    @Resource(name = "userService")
    SdUserService userService;

    @Resource(name = "ticketMasBeanPopulator")
    SdTicketMasBeanPopulator ticketMasBeanPopulator;
    @Resource(name = "ticketContactBeanPopulator")
    SdTicketContactBeanPopulator ticketContactBeanPopulator;

    @Override
    public Optional<SdTicketMasBean> createQueryTicket(String custCode) {
        SdTicketMasBean bean = new SdTicketMasBean();
        SdTicketMasEntity ticketMasEntity = new SdTicketMasEntity();
        ticketMasEntity.setCustCode(custCode);
        ticketMasEntity.setCreateby(userService.getCurrentUserUserId());
        ticketMasMapper.insertQueryTicket(ticketMasEntity);
        ticketMasBeanPopulator.populate(ticketMasEntity,bean);
        return Optional.of(bean);
    }

    @Override
    public Optional<SdTicketMasBean> getTicket(Integer ticketId) {
        SdTicketMasEntity ticketMasEntity = ticketMasMapper.findTicketById(ticketId);
        if (!ObjectUtils.isEmpty(ticketMasEntity)) {
            SdTicketMasBean bean = new SdTicketMasBean();
            ticketMasBeanPopulator.populate(ticketMasEntity,bean);
            return Optional.of(bean);
        }
        return Optional.empty();
    }

    @Override
    public void updateContactInfo(Integer ticketMasId, String contactType, String contactName, String contactNumber, String contactEmail, String contactMobile) {
        ticketContactMapper.insertTicketContactInfo(ticketMasId,contactType,contactName,contactMobile,contactEmail,contactNumber,userService.getCurrentUserUserId());
    }

    @Override
    public List<SdTicketContactBean> getContactInfo(Integer ticketMasId) {
        List<SdTicketContactBean> beanList = new ArrayList<>();
        ticketContactMapper.selectContactInfoByTicketMasId(ticketMasId).forEach(sdTicketContactEntity -> {
            SdTicketContactBean bean = new SdTicketContactBean();
            ticketContactBeanPopulator.populate(sdTicketContactEntity,bean);
            beanList.add(bean);
        });
        return beanList;
    }

    @Override
    public void removeContactInfoByTicketMasId(Integer ticketMasId) {
        ticketContactMapper.removeContactInfoByTicketMasId(ticketMasId);
    }
}
