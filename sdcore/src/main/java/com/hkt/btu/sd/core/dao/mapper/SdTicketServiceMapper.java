package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdTicketServiceEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SdTicketServiceMapper {

    List<SdTicketServiceEntity> getTicketServiceInfoByTicketMasId(@Param("ticketMasId") int ticketMasId);

    SdTicketServiceEntity getTicketServiceByTicketMasId(@Param("ticketMasId") int ticketMasId);

    void removeServiceInfoByTicketMasId(@Param("ticketMasId") int ticketMasId);

    void removeFaultsByTicketDetId(@Param("ticketDetId") int ticketDetId);

    void insertServiceInfo(SdTicketServiceEntity entity);

    void insertFaults(@Param("ticketDetId") Integer ticketDetId, @Param("faults")String faults,
                      @Param("createby") String createby, @Param("modifyby")String modifyby);

    void updateTicketServiceByJobId(@Param("jobId")Integer jobId, @Param("ticketMasId")String ticketMasId,@Param("userId") String userId);
}
