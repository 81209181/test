package com.hkt.btu.sd.core.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SdTicketFileUploadMapper {

    void insertUploadFile(@Param("ticketId")int ticketId, @Param("fileName")String fileName, @Param("file")byte[] file);
}
