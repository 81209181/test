package com.hkt.btu.sd.core.dao.mapper;

//import com.hkt.btu.sd.core.dao.entity.SdUserRoleEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.hkt.btu.sd.core.dao.entity.UTCallPageEntity;

import java.util.List;
import java.util.Map;

@Repository
public interface UTCallMapper {
    List<UTCallPageEntity> getUTCallRecord();
}