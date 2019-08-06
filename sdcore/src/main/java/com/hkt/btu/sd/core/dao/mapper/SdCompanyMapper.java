package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdCompanyEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdCompanyMapper {
    // select
    SdCompanyEntity getCompanyByCompanyId(Integer companyId);
    List<SdCompanyEntity> getCompany(@Param("status") String status);

    // paged select
    List<SdCompanyEntity> searchCompany(@Param("offset") long offset, @Param("pageSize") int pageSize,
                                         Integer companyId, String name);
    Integer countSearchCompany(Integer companyId, String name);

    // update
    int updateCompanyByCompanyId(Integer companyId, Integer modifyby, String name, String remarks);

    // insert
    void insertCompany(SdCompanyEntity sdCompanyEntity);
}
