package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdAccessRequestVisitorEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface SdAccessRequestVisitorMapper {
    // select
    List<SdAccessRequestVisitorEntity> getAccessRequestVisitorsByAccessRequestId(Integer accessRequestId, Integer companyId, Integer userId);

    // paged select
    List<SdAccessRequestVisitorEntity> searchVisitor(
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
    void insertVisitors(List<SdAccessRequestVisitorEntity> visitorEntityList);
}
