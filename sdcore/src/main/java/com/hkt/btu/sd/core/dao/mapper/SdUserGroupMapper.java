package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdUserGroupEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdUserGroupMapper {
    List<SdUserGroupEntity> getAllUserGroup();
    List<SdUserGroupEntity> getUserGroupByUserId(@Param("userId") Integer userId);

    void insertUserUserGroup(@Param("userId") Integer userId, @Param("groupId") String groupId, @Param("createby") Integer createby);
    void deleteUserUserGroup(@Param("userId") Integer userId, @Param("groupId") String groupId);
}
