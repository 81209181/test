package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdTicketUploadFileEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdTicketFileUploadMapper {

    void insertUploadFile(@Param("ticketId")int ticketId, @Param("fileName")String fileName, @Param("file")byte[] file);

    List<SdTicketUploadFileEntity> getUploadFiles(int ticketMasId);

    void removeUploadFileByTicketMasId(@Param("ticketMasId")Integer ticketMasId);
}
