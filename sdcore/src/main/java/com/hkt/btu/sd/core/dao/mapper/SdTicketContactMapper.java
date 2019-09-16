package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdTicketContactEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdTicketContactMapper {

    void insertTicketContactInfo(@Param("ticketMasId") Integer ticketMasId,
                                 @Param("contactType") String contactType,
                                 @Param("contactName") String contactName,
                                 @Param("contactMobile") String contactMobile,
                                 @Param("contactEmail") String contactEmail,
                                 @Param("contactNumber") String contactNumber,
                                 @Param("createBy") String createBy);

    List<SdTicketContactEntity> selectContactInfoByTicketMasId(@Param("ticketMasId") Integer ticketMasId);

    void removeContactInfoByTicketMasId(@Param("ticketMasId") Integer ticketMasId);
}
