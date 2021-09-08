package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdUserOwnerAuthRoleEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdUserOwnerAuthRoleMapper {

    List<SdUserOwnerAuthRoleEntity> getUserOwnerAuthRole(String ownerId);

    List<SdUserOwnerAuthRoleEntity> getUserOwnerAuthRoleList();

    SdUserOwnerAuthRoleEntity getUserOwnerAuthRoleBykey(@Param("ownerId") String ownerId,
                                                        @Param("authRoleId") String authRoleId,
                                                        @Param("serviceTypeCode") String serviceTypeCode);

    void createUserOwnerAuthRole(@Param("ownerId") String ownerId,
                                 @Param("authRoleId") String authRoleId,
                                 @Param("serviceTypeCode") String serviceTypeCode);

    void delUserOwnerAuthRole(@Param("ownerId") String ownerId,
                         @Param("authRoleId") String authRoleId,
                         @Param("serviceTypeCode") String serviceTypeCode);
}
