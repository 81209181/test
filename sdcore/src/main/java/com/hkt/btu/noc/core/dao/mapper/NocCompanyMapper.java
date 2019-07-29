package com.hkt.btu.noc.core.dao.mapper;

import com.hkt.btu.noc.core.dao.entity.NocCompanyEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NocCompanyMapper {
    // select
    NocCompanyEntity getCompanyByCompanyId(Integer companyId);
    List<NocCompanyEntity> getCompany(@Param("status") String status);

    // paged select
    List<NocCompanyEntity> searchCompany(@Param("offset") long offset, @Param("pageSize") int pageSize,
                                         Integer companyId, String name);
    Integer countSearchCompany(Integer companyId, String name);

    // update
    int updateCompanyByCompanyId(Integer companyId, Integer modifyby, String name, String remarks);

    // insert
    void insertCompany(NocCompanyEntity nocCompanyEntity);
}
