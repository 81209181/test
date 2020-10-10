package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdUserOwnerAuthRoleEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdUserOwnerAuthRoleMapper {

    List<SdUserOwnerAuthRoleEntity> getUserOwnerAuthRole(String ownerId);
}
