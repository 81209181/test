package com.hkt.btu.sd.facade;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.facade.data.RequestTicketServiceData;
import com.hkt.btu.sd.facade.data.SdTicketContactData;
import com.hkt.btu.sd.facade.data.SdTicketMasData;
import com.hkt.btu.sd.facade.data.SdTicketServiceData;
import com.hkt.btu.sd.facade.data.SdTicketRemarkData;
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

    List<SdTicketServiceData> getServiceInfo(Integer ticketMasId);

    String updateServiceInfo(List<RequestTicketServiceData> serviceList);

    List<SdTicketRemarkData> getRemarkInfo(Integer ticketMasId);

    void updateRemark(List<SdTicketRemarkData> remarkList);
}
