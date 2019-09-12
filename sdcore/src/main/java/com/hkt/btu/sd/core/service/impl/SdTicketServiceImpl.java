package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdTicketMasEntity;
import com.hkt.btu.sd.core.dao.mapper.SdTicketMasMapper;
import com.hkt.btu.sd.core.service.SdTicketService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdTicketMasBean;
import com.hkt.btu.sd.core.service.populator.SdTicketMasBeanPopulator;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Optional;

public class SdTicketServiceImpl implements SdTicketService {

    @Resource
    SdTicketMasMapper ticketMasMapper;

    @Resource(name = "userService")
    SdUserService userService;

    @Resource(name = "ticketMasBeanPopulator")
    SdTicketMasBeanPopulator ticketMasBeanPopulator;

    @Override
    public Optional<SdTicketMasBean> createQueryTicket(String custCode) {
        SdTicketMasBean bean = new SdTicketMasBean();
        SdTicketMasEntity ticketMasEntity = new SdTicketMasEntity();
        ticketMasEntity.setCustCode(custCode);
        ticketMasEntity.setCreateBy(userService.getCurrentUserUserId());
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
}
