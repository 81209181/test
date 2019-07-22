package com.hkt.btu.noc.core.dao.mapper;

import com.hkt.btu.noc.core.dao.entity.NocUserGroupEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NocUserGroupMapper {
    List<NocUserGroupEntity> getAllUserGroup();
    List<NocUserGroupEntity> getUserGroupByUserId(@Param("userId") Integer userId);

    void insertUserUserGroup(@Param("userId") Integer userId, @Param("groupId") String groupId, @Param("createby") Integer createby);
    void deleteUserUserGroup(@Param("userId") Integer userId, @Param("groupId") String groupId);
}
