package com.hkt.btu.sd.facade;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.core.exception.ApiException;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.data.cloud.HktCloudCaseData;
import com.hkt.btu.sd.facade.data.cloud.HktCloudViewData;
import com.hkt.btu.sd.facade.data.wfm.WfmMakeApptData;
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

    void createTicketRemarks(Integer ticketMasId, String remarks);

    void updateJobIdInService(Integer jobId, int ticketMasId);

    AppointmentData getAppointmentData(Integer ticketMasId);

    List<SdSymptomData> getSymptom(Integer ticketMasId);

    BesSubFaultData getFaultInfo(String subscriberId, Pageable pageable);

    SdTicketData getTicketInfo(Integer ticketMasId);

    List<SdTicketMasData> getPendingTicketList(String serviceNo);

    String closeTicketByApi(int ticketMasId, String reasonType, String reasonContent, String closeby);
    String closeTicket(int ticketMasId, String reasonType, String reasonContent, String contactName, String contactNumber);

    void isAllow(int ticketMasId,String action);

    boolean increaseCallInCount(Integer ticketMasId);

    void createJob4Wfm(int ticketMasId) throws InvalidInputException, ApiException;

    List<CodeDescData> getTicketStatusList();

    List<CodeDescData> getTicketTypeList();

    TeamSummaryData getTeamSummary();

    WfmMakeApptData getMakeApptDataByTicketDetId(Integer ticketDetId);

    String createTicket4hktCloud(HktCloudCaseData cloudCaseData);

    String getNewTicketId();

    List<HktCloudViewData> getHktCloudTicket(String tenantId, String username);

    List<SdTicketUploadFileData> getUploadFiles(int ticketMasId);
}
