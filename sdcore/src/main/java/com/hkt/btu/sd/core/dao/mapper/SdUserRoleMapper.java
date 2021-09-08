package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdUserRoleEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SdUserRoleMapper {

    List<SdUserRoleEntity> getTeamHeadList(@Param("indicator") String indicator);

    List<SdUserRoleEntity> getAllUserRole(@Param("status") String status);

    SdUserRoleEntity getUserRoleByRoleId(@Param("roleId") String roleId);

    List<SdUserRoleEntity> getUserRoleByUserIdAndStatus(@Param("userId") String userId, @Param("status") String status);

    List<SdUserRoleEntity> getParentRoleByRoleId(@Param("roleId") String roleId);

    List<SdUserRoleEntity> getEligibleRolesByCurrentUserRole(@Param("roleId") String roleId, @Param("status") String status);

    List<SdUserRoleEntity> getUserRoleByUserId(@Param("userId") String userId);

    void insertUserUserRole(@Param("userId") String userId, List<String> userRoleList);

    void deleteUserRoleByUserId(@Param("userId") String userId, List<String> userRoleList);

    void updateUserRole(@Param("roleId") String roleId, @Param("roleDesc") String roleDesc,
                        @Param("status") String status, @Param("abstractFlag") String abstractFlag,
                        @Param("modifyby") String modifyby);

    void updateUserUserRoleByUserId(@Param("oldUserId") String oldUserId, @Param("newUserId") String newUserId);

    List<String> getTeamRoleByUserId(@Param("userId") String userId);

    SdUserRoleEntity getTopUserRole();


    List<SdUserRoleEntity> getUserRoleByParentRoleId(@Param("roleId") String roleId);

    List<SdUserRoleEntity> getTeamHeadRoleList(@Param("roleId") String roleId);

    List<Map<String,Object>> getRole4Chart();

    List<SdUserRoleEntity> getRole4UserOwnerAuth();
}
