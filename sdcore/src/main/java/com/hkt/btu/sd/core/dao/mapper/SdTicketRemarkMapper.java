package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdTicketRemarkEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdTicketRemarkMapper {

    void insertTicketRemarks(@Param("ticketMasId") Integer ticketMasId, @Param("remarksType") String remarksType,
                             @Param("remarks") String remarks, @Param("createby") String createby);

    List<SdTicketRemarkEntity> getTicketRemarksByTicketId(@Param("ticketMasId") Integer ticketMasId,
                                                          @Param("remarksType") String remarksType);
}
