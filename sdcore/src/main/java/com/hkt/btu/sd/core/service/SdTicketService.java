package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.bean.*;
import com.hkt.btu.sd.core.service.constant.TicketStatusEnum;
import com.hkt.btu.sd.core.service.constant.TicketTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SdTicketService {
    int createQueryTicket(String custCode, String serviceNo, String serviceType, String subsId, String searchKey, String searchValue, String custName);

    Optional<SdTicketMasBean> getTicket(Integer ticketId);

    void insertTicketContactInfo(Integer ticketMasId, String contactType, String contactName, String contactNumber, String contactEmail, String contactMobile);

    List<SdTicketContactBean> getContactInfo(Integer ticketMasId);

    void removeContactInfoByTicketMasId(Integer ticketMasId);

    Page<SdTicketMasBean> searchTicketList(Pageable pageable, LocalDate createDateFrom, LocalDate createDateTo,
                                           String status, LocalDate completeDateFrom, LocalDate completeDateTo,
                                           String createBy, String ticketMasId, String custCode,
                                           String serviceNumber, String ticketType, String serviceType, boolean isReport, String owningRole);

    Page<SdTicketMasBean> getMyTicket(Pageable pageable);

    List<SdTicketServiceBean> getServiceInfo(Integer ticketMasId);

    int updateServiceInfo(SdTicketServiceBean bean);

    void updateServiceSymptom(Integer ticketMasId, String symptomCode);

    List<SdTicketRemarkBean> getTicketRemarksByTicketId(Integer ticketMasId);

    void createTicketCustRemarks(Integer ticketMasId, String remarks);
    void createTicketSysRemarks(Integer ticketMasId, String remarks);

    void updateJobIdInService(Integer jobId, int ticketMasId, String userId);

    void updateAppointment(String appointmentDate, boolean asap, String userId, String ticketMasId);

    boolean checkAppointmentDate(String appointmentDate);

    List<SdSymptomBean> getSymptomList(Integer ticketMasId);

    List<SdTicketServiceBean> findServiceBySubscriberId(String subscriberId, Pageable pageable);

    void updateTicketStatus(int ticketMasId, String status, String userId);

    void increaseCallInCount(Integer ticketMasId);

    List<SdTicketMasBean> getTicketByServiceNo(String serviceNom, String ticketType, String excludeStatus);
    List<SdTicketMasBean> getPendingTicketList(String serviceNo);

    void closeTicket(int ticketMasId, String reasonType, String reasonContent,
                     String contactName, String contactNumber, boolean nonApiClose) throws InvalidInputException;

    void updateTicketType(int ticketMasId, String job, String userId);

    List<TicketStatusEnum> getTicketStatusList();

    List<TicketTypeEnum> getTicketTypeList();

    TeamSummaryBean getTeamSummary();

    long countServiceBySubscriberId(String subscriberId);

    SdMakeApptBean getTicketServiceByDetId(Integer ticketDetId);

    String getNewTicketId();

    void createHktCloudTicket(int ticketId, String tenantId, String createdBy);

    void insertUploadFile(int ticketId, String fileName, String content);

    List<SdTicketMasBean> getHktCloudTicket(String tenantId, String username);

    List<SdTicketUploadFileBean> getUploadFiles(int ticketMasId);
}
