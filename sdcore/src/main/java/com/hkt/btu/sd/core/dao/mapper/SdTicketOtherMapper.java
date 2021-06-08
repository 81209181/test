package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdEvaAttributeEntity;
import com.hkt.btu.sd.core.dao.entity.SdGmbTicketEavEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SdTicketOtherMapper {

    void deleteEntityInt(@Param("ticketMasId") Integer ticketMasId);
    void deleteEntityVarchar(@Param("ticketMasId") Integer ticketMasId);

    void insertEntityInt(@Param("ticketMasId") Integer ticketMasId,
                         @Param("attributeId") Integer attributeId,
                         @Param("value") Integer value,
                         @Param("createby") String createby);
    void insertEntityVarchar(@Param("ticketMasId") Integer ticketMasId,
                             @Param("attributeId") Integer attributeId,
                             @Param("value") String value,
                             @Param("createby") String createby);

    SdEvaAttributeEntity getAttributeIdByName(@Param("attributeName") String attributeName);

    SdGmbTicketEavEntity getGmbTicketOtherInfo(@Param("ticketMasId") Integer ticketMasId);
}
