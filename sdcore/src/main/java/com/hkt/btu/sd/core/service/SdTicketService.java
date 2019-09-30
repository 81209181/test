package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SdTicketContactBean;
import com.hkt.btu.sd.core.service.bean.SdTicketMasBean;
import com.hkt.btu.sd.core.service.bean.SdTicketRemarkBean;
import com.hkt.btu.sd.core.service.bean.SdTicketServiceBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SdTicketService {
    int createQueryTicket(String custCode, String serviceNo, String serviceType, String subsId);

    Optional<SdTicketMasBean> getTicket(Integer ticketId);

    void insertTicketContactInfo(Integer ticketMasId, String contactType, String contactName, String contactNumber, String contactEmail, String contactMobile);

    List<SdTicketContactBean> getContactInfo(Integer ticketMasId);

    void removeContactInfoByTicketMasId(Integer ticketMasId);

    Page<SdTicketMasBean> searchTicketList(Pageable pageable, String dateFrom, String dateTo, String status);

    List<SdTicketMasBean> getMyTicket();

    List<SdTicketServiceBean> getServiceInfo(Integer ticketMasId);

    void removeServiceInfoByTicketMasId(Integer ticketMasId);

    int updateServiceInfo(SdTicketServiceBean bean);

    void updateFaultsInfo(Integer ticketDetId, String faults);

    List<SdTicketRemarkBean> getTicketRemarksByTicketId(Integer ticketMasId);

    void createTicketRemarks(Integer ticketMasId, String remarksType, String remarks);

    void updateJobIdInService(Integer jobId, String ticketMasId, String userId);

    Optional<SdTicketServiceBean> getService(Integer ticketId);

    void updateAppointment(String appointmentDate, boolean asap, String userId, String ticketMasId);

    boolean checkAppointmentDate(String appointmentDate);
}
