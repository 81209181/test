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
                                             @Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo,
                                             @Param("status") String status, @Param("ticketMasId") String ticketMasId, @Param("custCode") String custCode);

    Integer searchTicketCount(@Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo,
                              @Param("status") String status, @Param("ticketMasId") String ticketMasId, @Param("custCode") String custCode);

    List<SdTicketMasEntity> getMyTicket(@Param("createBy") String createBy);

    void updateAppointmentInMas(@Param("appointmentDate") LocalDateTime appointmentDate,
                                @Param("asap") String asap,
                                @Param("userId") String userId,
                                @Param("ticketMasId") String ticketMasId);

    void updateTicketStatus(@Param("ticketMasId") int ticketMasId,
                            @Param("status") String status,
                            @Param("userId") String userId);

    void updateTicketCallInCount(@Param("ticketMasId") Integer ticketMasId, @Param("userId") String userId);
}
