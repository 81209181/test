package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdUserGroupPathCtrlEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdUserGroupPathCtrlMapper {

    List<SdUserGroupPathCtrlEntity> getByStatus(@Param("status") String status);
}

