package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SdTicketMasBean;

import java.util.Optional;

public interface SdTicketService {
    Optional<SdTicketMasBean> createQueryTicket(String custCode);

    Optional<SdTicketMasBean> getTicket(Integer ticketId);
}
