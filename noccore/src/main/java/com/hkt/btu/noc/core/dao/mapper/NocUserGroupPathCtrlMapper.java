package com.hkt.btu.noc.core.dao.mapper;

import com.hkt.btu.noc.core.dao.entity.NocUserGroupPathCtrlEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NocUserGroupPathCtrlMapper {

    List<NocUserGroupPathCtrlEntity> getByStatus(@Param("status") String status);
}

