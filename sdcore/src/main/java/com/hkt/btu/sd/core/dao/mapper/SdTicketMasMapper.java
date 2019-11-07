package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdTicketMasEntity;
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
                                             @Param("custCode") String ticketType,
                                             @Param("serviceNumber") String serviceType);

    Integer searchTicketCount(@Param("createDateFrom") LocalDate createDateFrom,
                              @Param("createDateTo") LocalDate createDateTo,
                              @Param("status") String status,
                              @Param("completeDateFrom") LocalDate completeDateFrom,
                              @Param("completeDateTo") LocalDate completeDateTo,
                              @Param("createBy") String createBy,
                              @Param("ticketMasId") String ticketMasId,
                              @Param("custCode") String custCode,
                              @Param("serviceNumber") String serviceNumber,
                              @Param("custCode") String ticketType,
                              @Param("serviceNumber") String serviceType);

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
