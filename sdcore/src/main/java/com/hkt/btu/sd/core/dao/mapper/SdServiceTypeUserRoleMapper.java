package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdServiceTypeUserRoleEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdServiceTypeUserRoleMapper {

    void createServiceTypeUserRole(List<String> userRoleId,
                                   @Param("serviceType") String serviceType,
                                   @Param("createby") String createby);

    void deleteServiceTypeUserRole(@Param("serviceType") String serviceType, List<String> userRoleId);

    List<SdServiceTypeUserRoleEntity> getServiceTypeUserRole(@Param("serviceType") String serviceType);
}
