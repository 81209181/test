package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdUserRoleWorkgroupEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdUserRoleWorkgroupMapper {
    List<SdUserRoleWorkgroupEntity> getUserRoleWorkgroupList();

    SdUserRoleWorkgroupEntity getUserRoleWorkgroupBykey(@Param("roleId") String roleId, @Param("workgroup") String workgroup);

    void delUserRoleWorkgroup(@Param("roleId") String roleId, @Param("workgroup") String workgroup);

    void createUserRoleWorkgroup(@Param("roleId") String roleId, @Param("workgroup") String workgroup, @Param("userId") String userId);

    List<String> getWorkgroupList();
}
