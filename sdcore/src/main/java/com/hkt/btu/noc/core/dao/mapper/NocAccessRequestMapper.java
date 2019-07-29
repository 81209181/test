package com.hkt.btu.noc.core.dao.mapper;

import com.hkt.btu.noc.core.dao.entity.NocAccessRequestEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface NocAccessRequestMapper {
    // select
    NocAccessRequestEntity getAccessRequest(Integer accessRequestId, Integer companyId, Integer userId);
    NocAccessRequestEntity getAccessRequestByVisitorAccessId(Integer visitorAccessId);
    List<NocAccessRequestEntity> getExpiredAccessRequest(String completeStatus);
    Integer getMaxAccessRequestId();

    // paged select
    List<NocAccessRequestEntity> searchAccessRequest(
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
    void insertAccessRequest(NocAccessRequestEntity nocAccessRequestEntity);
}
