package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.bean.*;
import com.hkt.btu.sd.core.service.constant.TicketStatusEnum;
import com.hkt.btu.sd.core.service.constant.TicketTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SdTicketService {

    // get ticket constant
    List<TicketStatusEnum> getTicketStatusList();
    List<TicketTypeEnum> getTicketTypeList();

    // get ticket profile
    Page<SdTicketMasBean> searchTicketList(Pageable pageable, LocalDate createDateFrom, LocalDate createDateTo,
                                           String status, LocalDate completeDateFrom, LocalDate completeDateTo,
                                           String createBy, String ticketMasId, String custCode,
                                           String serviceNumber, String serviceNumberExact, String ticketType,
                                           String serviceType,  boolean isReport, String owningRole);
    Page<SdTicketMasBean> getMyTicket(Pageable pageable);
    SdTeamSummaryBean getTeamSummary();
    List<SdTicketMasBean> getHktCloudTicket(String tenantId, String username);
    List<SdTicketMasBean> getTicketByServiceNo(String serviceNo, String ticketType, String excludeStatus);
    List<SdTicketMasBean> getPendingTicketList(String serviceNo);
    boolean isMatchTicketJobType(Integer ticketMasId, TicketTypeEnum ticketTypeEnum);
    boolean isMatchTicketServiceType(Integer ticketMasId, String ticketServiceType);

    // get ticket per subscriber
    long countServiceBySubscriberId(String subscriberId);
    List<SdTicketServiceBean> findServiceBySubscriberId(String subscriberId, Pageable pageable);

    // get ticket detail
    Optional<SdTicketMasBean> getTicket(Integer ticketMasId);
    List<SdTicketContactBean> getContactInfo(Integer ticketMasId);
    List<SdTicketServiceBean> getServiceInfo(Integer ticketMasId);
    List<SdSymptomBean> getSymptomList(Integer ticketMasId);

    SdMakeApptBean getTicketServiceByDetId(Integer ticketDetId);
    List<SdTicketRemarkBean> getTicketRemarksByTicketId(Integer ticketMasId);
    List<SdTicketUploadFileBean> getUploadFiles(int ticketMasId);

    // create ticket
    int createQueryTicket(String custCode, String serviceNo, String serviceType, String subsId, String searchKey, String searchValue, String custName);
    void createHktCloudTicket(int ticketId, String tenantId, String createdBy);

    // create/delete ticket detail
    String getNewTicketId();
    void insertTicketContactInfo(Integer ticketMasId, String contactType, String contactName, String contactNumber, String contactEmail, String contactMobile);
    void removeContactInfoByTicketMasId(Integer ticketMasId);
    void createTicketCustRemarks(Integer ticketMasId, String remarks);
    void createTicketSysRemarks(Integer ticketMasId, String remarks);
    void insertUploadFile(int ticketId, String fileName, String content);
    void updateServiceSymptom(Integer ticketMasId, String symptomCode, LocalDateTime reportTime);
    void updateJobIdInService(Integer jobId, int ticketMasId, String userId);
    void increaseCallInCount(Integer ticketMasId);
    void closeTicket(int ticketMasId, String reasonType, String reasonContent,
                     String contactName, String contactNumber, boolean nonApiClose) throws InvalidInputException;
    void updateTicketType(int ticketMasId, String job, String userId);

}
