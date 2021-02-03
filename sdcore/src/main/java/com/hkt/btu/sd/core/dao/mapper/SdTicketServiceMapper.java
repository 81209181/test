package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdMakeApptEntitiy;
import com.hkt.btu.sd.core.dao.entity.SdSymptomEntity;
import com.hkt.btu.sd.core.dao.entity.SdTicketServiceEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SdTicketServiceMapper {

    List<SdTicketServiceEntity> getTicketServiceInfoByTicketMasId(@Param("ticketMasId") int ticketMasId);

    void removeServiceInfoByTicketMasId(@Param("ticketMasId") int ticketMasId);

    void insertServiceInfo(SdTicketServiceEntity entity);

    void updateTicketServiceByJobId(@Param("jobId") Integer jobId, @Param("ticketMasId") int ticketMasId, @Param("userId") String userId);

    void updateTicketServiceSymptomByTicketMasId(@Param("ticketMasId") Integer ticketMasId,
                                                 @Param("symptomCode") String symptomCode,
                                                 @Param("modifyby") String modifyby,
                                                 @Param("reportTime") LocalDateTime reportTime);

    List<SdSymptomEntity> getSymptomListByTicketMasId(Integer ticketMasId);

    List<SdTicketServiceEntity> getTicketServiceBySubscriberId(@Param("subscriberId") String subscriberId,@Param("offset") Long offset, @Param("pageSize") Integer pageSize);

    long countServiceBySubscriberId(@Param("subscriberId")String subscriberId);

    SdMakeApptEntitiy getTicketServiceByTicketDetId(@Param("ticketDetId") Integer ticketDetId);

    void insertServiceInfoForCloudCase(@Param("ticketId") int ticketId, @Param("createdBy") String createdBy,@Param("tenantId") String tenantId);

    void updateTicketServiceWfmCompInfo(@Param("ticketMasId") Integer ticketMasId, @Param("wfmCompleteInfo") String wfmCompleteInfo, @Param("modifyby") String modifyby);

    void updateTicketServiceCloseCode(@Param("ticketMasId") Integer ticketMasId, @Param("closeCode") String closeCode, @Param("modifyby") String userUserId);
}
