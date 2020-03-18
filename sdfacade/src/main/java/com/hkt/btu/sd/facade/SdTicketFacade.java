package com.hkt.btu.sd.facade;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.facade.data.BtuCodeDescData;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.core.exception.ApiException;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.data.bes.BesSubFaultData;
import com.hkt.btu.sd.facade.data.cloud.HktCloudCaseData;
import com.hkt.btu.sd.facade.data.cloud.HktCloudViewData;
import com.hkt.btu.sd.facade.data.wfm.WfmMakeApptData;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SdTicketFacade {

    // constant list
    List<BtuCodeDescData> getTicketStatusList();
    List<BtuCodeDescData> getTicketTypeList();

    // ticket
    String getNewTicketId();
    Optional<SdTicketMasData> getTicket(Integer ticketId);
    PageData<SdTicketMasData> searchTicketList(Pageable pageable, Map<String, String> searchFormData);
    PageData<SdTicketMasData> getMyTicket(Pageable pageable);
    List<SdTicketMasData> getPendingTicketList(String serviceNo);

    SdTicketData getTicketInfo(Integer ticketMasId);
    List<HktCloudViewData> getHktCloudTicket(String tenantId, String username);
    BesSubFaultData getFaultInfo(String subscriberId, Pageable pageable);

    void isAllow(int ticketMasId, String action);

    int createQueryTicket(SdQueryTicketRequestData queryTicketRequestData);
    void createJob4Wfm(int ticketMasId) throws InvalidInputException, ApiException;
    String createTicket4hktCloud(HktCloudCaseData cloudCaseData);

    boolean increaseCallInCount(Integer ticketMasId);
    String closeTicket(int ticketMasId, String reasonType, String reasonContent, String contactName, String contactNumber);
    String closeTicketByApi(int ticketMasId, String reasonType, String reasonContent, String closeby);

    // ticket contact
    List<SdTicketContactData> getContactInfo(Integer ticketMasId);

    String updateContactInfo(List<SdTicketContactData> contactList);

    // ticket service
    List<SdTicketServiceData> getServiceInfo(Integer ticketMasId);
    SdAppointmentData getAppointmentData(Integer ticketMasId);
    WfmMakeApptData getMakeApptDataByTicketDetId(Integer ticketDetId);
    List<SdSymptomData> getSymptom(Integer ticketMasId);

    String updateServiceInfo(List<SdRequestTicketServiceData> serviceList);
    void updateJobIdInService(Integer jobId, int ticketMasId);

    // ticket remarks
    List<SdTicketRemarkData> getTicketRemarksByTicketId(Integer ticketMasId);

    void createTicketRemarks(Integer ticketMasId, String remarks);

    // ticket others
    List<SdTicketUploadFileData> getUploadFiles(int ticketMasId);

    // statistics
    SdTeamSummaryData getTeamSummary();

}
