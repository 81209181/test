package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdTicketRemarkEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdTicketRemarkMapper {

    void insertTicketRemarks(Integer ticketMasId, String remarksType, String remarks, String createBy);

    List<SdTicketRemarkEntity> getTicketRemarksByTicketId(@Param("ticketMasId") Integer ticketMasId);

    void removeTicketRemarks(@Param("ticketMasId") Integer ticketMasId);
}
