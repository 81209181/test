package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdTicketMasEntity;
import com.hkt.btu.sd.core.dao.entity.SdTicketStatisticEntity;
import com.hkt.btu.sd.core.dao.entity.StatusSummaryEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SdTicketMasMapper {

    int insertQueryTicket(SdTicketMasEntity ticketMasEntity);

    SdTicketMasEntity findTicketById(@Param("ticketId") Integer ticketId);

    List<SdTicketMasEntity> searchTicketList(@Param("offset") long offset, @Param("pageSize") int pageSize,
                                             @Param("createDateFrom") LocalDate createDateFrom,
                                             @Param("createDateTo") LocalDate createDateTo,
                                             @Param("status") String status,
                                             @Param("completeDateFrom") LocalDate completeDateFrom,
                                             @Param("completeDateTo") LocalDate completeDateTo,
                                             @Param("createBy") String createBy,
                                             @Param("ticketMasId") String ticketMasId,
                                             @Param("custCode") String custCode,
                                             @Param("serviceNumber") String serviceNumber,
                                             @Param("serviceNumberExact") String serviceNumberExact,
                                             @Param("ticketType") String ticketType,
                                             @Param("serviceType") String serviceType,
                                             @Param("owningRole") String owningRole);

    Integer searchTicketCount(@Param("createDateFrom") LocalDate createDateFrom,
                              @Param("createDateTo") LocalDate createDateTo,
                              @Param("status") String status,
                              @Param("completeDateFrom") LocalDate completeDateFrom,
                              @Param("completeDateTo") LocalDate completeDateTo,
                              @Param("createBy") String createBy,
                              @Param("ticketMasId") String ticketMasId,
                              @Param("custCode") String custCode,
                              @Param("serviceNumber") String serviceNumber,
                              @Param("serviceNumberExact") String serviceNumberExact,
                              @Param("ticketType") String ticketType,
                              @Param("serviceType") String serviceType,
                              @Param("owningRole") String owningRole);

    void updateAppointmentInMas(@Param("appointmentDate") LocalDateTime appointmentDate,
                                @Param("asap") String asap,
                                @Param("userId") String userId,
                                @Param("ticketMasId") String ticketMasId);

    void updateTicketStatus(@Param("ticketMasId") int ticketMasId,
                            @Param("status") String status,
                            @Param("arrivalTime") LocalDateTime arrivalTime,
                            @Param("completeTime") LocalDateTime completeTime,
                            @Param("userId") String userId);

    void updateTicketCallInCount(@Param("ticketMasId") Integer ticketMasId, @Param("userId") String userId);

    List<SdTicketMasEntity> getTicketByServiceNo(@Param("serviceType")String serviceType,
                                                 @Param("serviceNo")String serviceNo,
                                                 @Param("ticketType")String ticketType,
                                                 @Param("excludeStatus")String excludeStatus);

    void updateTicketType(@Param("ticketMasId")int ticketMasId, @Param("type")String type,  @Param("userId")String userId);

    List<StatusSummaryEntity> getCountStatusByTicketType(@Param("owningRole")String owningRole);
    StatusSummaryEntity getSumStatusByTicketType(@Param("owningRole")String owningRole);


    String getNewTicketId();

    void createHktCloudTicket(@Param("ticketId")int ticketId, @Param("createBy")String createBy, @Param("tenantId")String tenantId);

    List<SdTicketMasEntity> getTicket4HktCloud(@Param("tenantId") String tenantId, @Param("username")String username);

    List<SdTicketStatisticEntity> ticketTypeCountPerOwnerGroup();

    List<SdTicketStatisticEntity> ticketStatusCountPerOwnerGroup();
}
