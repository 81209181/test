package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdUserRoleEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdUserRoleMapper {

    List<SdUserRoleEntity> getTeamHeadList(@Param("indicator") String indicator);

    List<SdUserRoleEntity> getAllUserRole(@Param("status") String status);

    SdUserRoleEntity getUserRoleByRoleId(@Param("roleId") String roleId);

    List<SdUserRoleEntity> getUserRoleByUserId(@Param("userId") String userId);

    List<SdUserRoleEntity> getParentRoleByRoleId(@Param("roleId") String roleId);

    List<SdUserRoleEntity> getEligibleRolesByCurrentUserRole(@Param("roleId") String roleId, @Param("status") String status);

    void insertUserUserRole(@Param("userId") String userId, @Param("roleId") String roleId);

    void deleteUserRoleByUserId(@Param("userId") String userId);

    int updateUserRole(@Param("roleId") String roleId, @Param("roleDesc") String roleDesc, @Param("status") String status, @Param("modifyby") String modifyby);
}
