package com.hkt.btu.noc.core.dao.mapper;

import com.hkt.btu.noc.core.dao.entity.NocAccessRequestVisitorEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface NocAccessRequestVisitorMapper {
    // select
    List<NocAccessRequestVisitorEntity> getAccessRequestVisitorsByAccessRequestId(Integer accessRequestId, Integer companyId, Integer userId);

    // paged select
    List<NocAccessRequestVisitorEntity> searchVisitor(
            @Param("offset") long offset, @Param("pageSize") int pageSize,
            Integer companyIdRestriction, Integer userIdRestriction,
            String visitorName, String companyName,
            String visitLocation, LocalDate visitDateFrom, LocalDate visitDateTo);
    Integer countSearchVisitor(Integer companyIdRestriction, Integer userIdRestriction,
                               String visitorName, String companyName,
                               String visitLocation, LocalDate visitDateFrom, LocalDate visitDateTo);

    // update
    int checkin(Integer visitorAccessId, String visitorCardNum);
    int checkout(Integer visitorAccessId);


    // insert
    void insertVisitors(List<NocAccessRequestVisitorEntity> visitorEntityList);
}
