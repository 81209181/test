package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.data.SdTicketMasData;

import java.util.Optional;

public interface SdTicketFacade {
    Optional<SdTicketMasData> createQueryTicket(String custCode);

    Optional<SdTicketMasData> getTicket(Integer ticketId);
}
