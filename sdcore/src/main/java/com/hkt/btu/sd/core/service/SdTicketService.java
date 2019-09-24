package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.dao.entity.SdTicketServiceEntity;
import com.hkt.btu.sd.core.service.bean.SdTicketContactBean;
import com.hkt.btu.sd.core.service.bean.SdTicketMasBean;
import com.hkt.btu.sd.core.service.bean.SdTicketServiceBean;
import com.hkt.btu.sd.core.service.bean.SdTicketRemarkBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SdTicketService {
    int createQueryTicket(String custCode, String serviceNo, String serviceType);

    Optional<SdTicketMasBean> getTicket(Integer ticketId);

    void updateContactInfo(Integer ticketMasId, String contactType, String contactName, String contactNumber, String contactEmail, String contactMobile);

    List<SdTicketContactBean> getContactInfo(Integer ticketMasId);

    void removeContactInfoByTicketMasId(Integer ticketMasId);

    Page<SdTicketMasBean> searchTicketList(Pageable pageable, String dateFrom, String dateTo, String status);

    List<SdTicketMasBean> getMyTicket();

    List<SdTicketServiceBean> getServiceInfo(Integer ticketMasId);

    void removeServiceInfoByTicketMasId(Integer ticketMasId);

    int updateServiceInfo(SdTicketServiceBean bean);

    void updateFaultsInfo(Integer ticketDetId, String faults);

    void updateRemark(Integer ticketMasId, String remarksType, String remarks);

    void removeRemarkByTicketMasId(Integer ticketMasId);

    List<SdTicketRemarkBean> getRemarkInfo(Integer ticketMasId);
}
