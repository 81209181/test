package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdTicketMasEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdTicketMasMapper {

    int insertQueryTicket(SdTicketMasEntity ticketMasEntity);

    SdTicketMasEntity findTicketById(@Param("ticketId") Integer ticketId);

    List<SdTicketMasEntity> searchTicketList(@Param("offset") long offset, @Param("pageSize") int pageSize,
                                             @Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo,
                                             @Param("status") String status);

    Integer searchTicketCount(@Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo,
                              @Param("status") String status);

    List<SdTicketMasEntity> getMyTicket(@Param("createBy") String createBy);
}
