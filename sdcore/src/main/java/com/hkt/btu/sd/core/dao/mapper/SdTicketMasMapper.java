package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdTicketMasEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SdTicketMasMapper {

    int insertQueryTicket(SdTicketMasEntity ticketMasEntity);

    SdTicketMasEntity findTicketById(@Param("ticketId") Integer ticketId);

    List<SdTicketMasEntity> searchTicketList(@Param("offset") long offset, @Param("pageSize") int pageSize,
                                             @Param("createDateFrom") String createDateFrom, // todo [SERVDESK-200]: use LocalDate instead of String
                                             @Param("createDateTo") String createDateTo, // todo [SERVDESK-200]: use LocalDate instead of String
                                             @Param("status") String status,
                                             @Param("completeDateFrom") String completeDateFrom, // todo [SERVDESK-200]: use LocalDate instead of String
                                             @Param("completeDateTo") String completeDateTo, // todo [SERVDESK-200]: use LocalDate instead of String
                                             @Param("createBy") String createBy,
                                             @Param("ticketMasId") String ticketMasId,
                                             @Param("custCode") String custCode,
                                             @Param("serviceNumber") String serviceNumber);

    Integer searchTicketCount(@Param("createDateFrom") String createDateFrom,
                              @Param("createDateTo") String createDateTo,
                              @Param("status") String status,
                              @Param("completeDateFrom") String completeDateFrom,
                              @Param("completeDateTo") String completeDateTo,
                              @Param("createBy") String createBy,
                              @Param("ticketMasId") String ticketMasId,
                              @Param("custCode") String custCode,
                              @Param("serviceNumber") String serviceNumber);

    List<SdTicketMasEntity> getMyTicket(@Param("offset") long offset, @Param("pageSize") int pageSize,
                                             @Param("createDateFrom") String createDateFrom,
                                             @Param("createDateTo") String createDateTo,
                                             @Param("status") String status,
                                             @Param("completeDateFrom") String completeDateFrom,
                                             @Param("completeDateTo") String completeDateTo,
                                             @Param("createBy") String createBy,
                                             @Param("ticketMasId") String ticketMasId,
                                             @Param("custCode") String custCode,
                                             @Param("serviceNumber") String serviceNumber);

    void updateAppointmentInMas(@Param("appointmentDate") LocalDateTime appointmentDate,
                                @Param("asap") String asap,
                                @Param("userId") String userId,
                                @Param("ticketMasId") String ticketMasId);

    void updateTicketStatus(@Param("ticketMasId") int ticketMasId,
                            @Param("status") String status,
                            @Param("userId") String userId);

    void updateTicketCallInCount(@Param("ticketMasId") Integer ticketMasId, @Param("userId") String userId);

    List<SdTicketMasEntity> getTicketByServiceNo(@Param("serviceNo")String serviceNo, @Param("status")String status);

    void updateTicketType(@Param("ticketMasId")int ticketMasId, @Param("type")String type,  @Param("userId")String userId);

    List<Integer> getTicketByServiceNoAndTypeNotJobAndStatusNotCP(@Param("serviceNo")String serviceNo);
}
