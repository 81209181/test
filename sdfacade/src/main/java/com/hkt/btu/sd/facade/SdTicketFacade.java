package com.hkt.btu.sd.facade;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.facade.data.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SdTicketFacade {
    int createQueryTicket(QueryTicketRequestData queryTicketRequestData);

    Optional<SdTicketMasData> getTicket(Integer ticketId);

    String updateContactInfo(List<SdTicketContactData> contactList);

    List<SdTicketContactData> getContactInfo(Integer ticketMasId);

    PageData<SdTicketMasData> searchTicketList(Pageable pageable, Map<String, String> searchFormData);

    PageData<SdTicketMasData> getMyTicket(Pageable pageable);

    List<SdTicketServiceData> getServiceInfo(Integer ticketMasId);

    String updateServiceInfo(List<RequestTicketServiceData> serviceList);

    List<SdTicketRemarkData> getTicketRemarksByTicketId(Integer ticketMasId);

    String createTicketRemarks(Integer ticketMasId, String remarks);

    void updateJobIdInService(Integer jobId, int ticketMasId);

    SdTicketServiceData getService(Integer ticketId);

    AppointmentData getAppointmentData(Integer ticketMasId);

    List<SdSymptomData> getSymptom(Integer ticketMasId);

    BesSubFaultData getFaultInfo(String subscriberId);

    SdTicketData getTicketInfo(Integer ticketMasId);

    List<SdTicketMasData> getPendingTicketList(String serviceNo);

    String closeTicketByApi(int ticketMasId, String reasonType, String reasonContent, String closeby);
    String closeTicket(int ticketMasId, String reasonType, String reasonContent, String contactName, String contactNumber);

    void isAllow(String ticketMasId,String action);

    boolean increaseCallInCount(Integer ticketMasId);

    void createJob4Wfm(int ticketMasId);

    List<CodeDescData> getTicketStatusList();

    List<CodeDescData> getTicketTypeList();

    TeamSummaryData getTeamSummary();
}
