package com.hkt.btu.common.core.dao.mapper;

import com.hkt.btu.common.core.dao.entity.BtuUserGroupEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BtuUserGroupMapper {
    List<BtuUserGroupEntity> getAllUserGroup();
    List<BtuUserGroupEntity> getUserGroupByUserId(@Param("userId") Integer userId);

    void insertUserUserGroup(@Param("userId") Integer userId, @Param("groupId") String groupId, @Param("createby") Integer createby);
    void deleteUserUserGroup(@Param("userId") Integer userId, @Param("groupId") String groupId);
}
