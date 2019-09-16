package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SdTicketContactBean;
import com.hkt.btu.sd.core.service.bean.SdTicketMasBean;

import java.util.List;
import java.util.Optional;

public interface SdTicketService {
    Optional<SdTicketMasBean> createQueryTicket(String custCode);

    Optional<SdTicketMasBean> getTicket(Integer ticketId);

    void updateContactInfo(Integer ticketMasId, String contactType, String contactName, String contactNumber, String contactEmail, String contactMobile);

    List<SdTicketContactBean> getContactInfo(Integer ticketMasId);

    void removeContactInfoByTicketMasId(Integer ticketMasId);
}
