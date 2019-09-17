package com.hkt.btu.sd.facade;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.facade.data.SdTicketContactData;
import com.hkt.btu.sd.facade.data.SdTicketMasData;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SdTicketFacade {
    Optional<SdTicketMasData> createQueryTicket(String custCode);

    Optional<SdTicketMasData> getTicket(Integer ticketId);

    void updateContactInfo(List<SdTicketContactData> contactList);

    List<SdTicketContactData> getContactInfo(Integer ticketMasId);

    PageData<SdTicketMasData> searchTicketList(Pageable pageable, String dateFrom, String dateTo, String status);

    List<SdTicketMasData> getMyTicket();
}
