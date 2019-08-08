package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdAccessRequestEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface SdAccessRequestMapper {
    // select
    SdAccessRequestEntity getAccessRequest(Integer accessRequestId, Integer companyId, Integer userId);
    SdAccessRequestEntity getAccessRequestByVisitorAccessId(Integer visitorAccessId);
    List<SdAccessRequestEntity> getExpiredAccessRequest(String completeStatus);
    Integer getMaxAccessRequestId();

    // paged select
    List<SdAccessRequestEntity> searchAccessRequest(
            @Param("offset") long offset, @Param("pageSize") int pageSize,
            Integer companyId, Integer userId,
            Integer accessRequestId, String companyName, String status,
            String visitLocation, LocalDate visitDateFrom, LocalDate visitDateTo);
    Integer countSearchAccessRequest(Integer companyId, Integer userId,
                                     Integer accessRequestId, String companyName, String status,
                                     String visitLocation, LocalDate visitDateFrom, LocalDate visitDateTo);

    // update
    int updateStatus(Integer accessRequestId, String newStatus, Integer modifyby);

    // insert
    void insertAccessRequest(SdAccessRequestEntity sdAccessRequestEntity);
}
