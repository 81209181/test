package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdUserRoleEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdUserRoleMapper {

    List<SdUserRoleEntity> getAllUserRole();

    List<SdUserRoleEntity> getUserRoleByUserId(@Param("userId") String userId);

    List<SdUserRoleEntity> getParentRoleByRoleId(@Param("roleId") String roleId);

    void insertUserUserRole(@Param("userId") String userId, @Param("roleId") String roleId);

    void deleteUserRoleByUserId(@Param("userId") String userId);
}
