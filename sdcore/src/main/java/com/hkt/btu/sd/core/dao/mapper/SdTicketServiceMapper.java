package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdTicketServiceEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdTicketServiceMapper {

    List<SdTicketServiceEntity> getTicketServiceInfoByTicketMasId(@Param("ticketMasId") int ticketMasId);
}
