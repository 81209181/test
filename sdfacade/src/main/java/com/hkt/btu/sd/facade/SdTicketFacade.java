package com.hkt.btu.sd.facade;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.facade.data.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SdTicketFacade {
    int createQueryTicket(String custCode, String serviceNo, String serviceType, String subsId);

    Optional<SdTicketMasData> getTicket(Integer ticketId);

    String updateContactInfo(List<SdTicketContactData> contactList);

    List<SdTicketContactData> getContactInfo(Integer ticketMasId);

    PageData<SdTicketMasData> searchTicketList(Pageable pageable, String dateFrom, String dateTo, String status, String ticketMasId, String custCode);

    List<SdTicketMasData> getMyTicket();

    List<SdTicketServiceData> getServiceInfo(Integer ticketMasId);

    String updateServiceInfo(List<RequestTicketServiceData> serviceList);

    List<SdTicketRemarkData> getTicketRemarksByTicketId(Integer ticketMasId);

    String createTicketRemarks(Integer ticketMasId, String remarks);

    void updateJobIdInService(Integer jobId, String ticketMasId, String name);

    Optional<SdTicketServiceData> getService(Integer ticketId);

    void updateAppointment(String appointmentDate, boolean asap, String name, String ticketMasId);

    boolean checkAppointmentDate(String appointmentDate);

    List<SdSymptomData> getSymptom(Integer ticketMasId);

    BesSubFaultData getFaultInfo(String subscriberId);

    SdTicketData getTicketInfo(Integer ticketMasId);

    void cancelTicket(int ticketMasId, String name);

    boolean isCancel(String ticketMasId);

    List<SdTicketMasData> getTicketByServiceNo(String serviceNo);
}
