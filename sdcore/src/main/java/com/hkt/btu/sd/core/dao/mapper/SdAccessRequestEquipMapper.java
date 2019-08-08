package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdAccessRequestEquipEntity;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SdAccessRequestEquipMapper {
    // select
    List<SdAccessRequestEquipEntity> getAccessRequestEquipsByAccessRequestId(Integer accessRequestId, Integer companyId, Integer userId);

    // paged select


    // update


    // insert
    void insertEquips(List<SdAccessRequestEquipEntity> equipEntityList);
}
