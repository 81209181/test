package com.hkt.btu.sd.facade;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.facade.data.BtuCodeDescData;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.facade.data.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
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
    List<SdTicketMasData> getPendingTicketList(String serviceType, String serviceNo);
    SdTicketMasData getTicketMas(Integer ticketMasId);

    SdTicketData getTicketInfo(Integer ticketMasId);

    void isAllow(int ticketMasId, String action);

    int createQueryTicket(SdQueryTicketRequestData queryTicketRequestData);
    void createJob4Wfm(int ticketMasId, boolean notifyOss) throws InvalidInputException;

    boolean increaseCallInCount(Integer ticketMasId);
    String closeTicket(int ticketMasId, String closeCode, String reasonType, String reasonContent, String contactName, String contactNumber);

    // ticket contact
    List<SdTicketContactData> getContactInfo(Integer ticketMasId);

    String updateContactInfo(List<SdTicketContactData> contactList);

    // ticket service
    List<SdTicketServiceData> getServiceInfo(Integer ticketMasId);
    SdAppointmentData getAppointmentData(Integer ticketMasId);
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

    List<SdCloseCodeData> getCloseCode(String serviceType);

    String getSymptomForApi(String serviceType, List<String> workingPartyList);

    PageData<SdTicketMasData> searchBchspList(Pageable pageable, Map<String, String> searchFormData);

    String getJobId(String ticketMasId);

    List<String> getWorkGroupList();

    List<SdTicketExportData> searchTicketListForExport(Map<String, String> searchFormData);

    void fillSheet(HSSFSheet access_request, List<SdTicketExportData> accessRequestDataList);

    String getFileName();

    List<SdOutstandingFaultData> getOutstandingFault();

    List<SdTicketTimePeriodSummaryData> getTicketTimePeriodSummary();

    String getAvgFaultCleaningTime();

    void createJob4Wfm(Integer ticketMasId, List<SdTicketContactData> contactList, List<SdRequestTicketServiceData> serviceList, String remarks) throws InvalidInputException, RuntimeException;

    List<SdTicketRemarkData> getTicketCustRemarks(Integer ticketMasId);
}
