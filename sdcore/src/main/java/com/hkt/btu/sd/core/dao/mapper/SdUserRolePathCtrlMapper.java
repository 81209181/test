package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdUserRolePathCtrlEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdUserRolePathCtrlMapper {

    List<SdUserRolePathCtrlEntity> getByStatus(@Param("status") String status);

    List<SdUserRolePathCtrlEntity> getParentRolePathByRoleId(@Param("roleId") String roleId, @Param("status") String status);
}
