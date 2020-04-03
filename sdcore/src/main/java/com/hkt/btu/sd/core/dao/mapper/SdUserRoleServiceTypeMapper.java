package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdUserRoleServiceTypeEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdUserRoleServiceTypeMapper {

    void createUserRoleServiceType(List<String> serviceTypeList,
                                   @Param("roleId") String roleId,
                                   @Param("createby") String createby);

    void deleteUserRoleServiceType(@Param("roleId") String roleId, List<String> serviceTypeList);

    List<SdUserRoleServiceTypeEntity> getUserRoleServiceType(@Param("roleId") String roleId);
}
