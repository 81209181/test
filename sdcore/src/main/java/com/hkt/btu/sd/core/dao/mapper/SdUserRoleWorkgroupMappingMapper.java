package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdUserRoleWorkgroupMappingEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdUserRoleWorkgroupMappingMapper {
    List<SdUserRoleWorkgroupMappingEntity> getUserRoleWorkgroupMappingList();

    SdUserRoleWorkgroupMappingEntity getUserRoleWorkgroupMappingBykey();

    void delUserRoleWorkgroupMapping();
}
