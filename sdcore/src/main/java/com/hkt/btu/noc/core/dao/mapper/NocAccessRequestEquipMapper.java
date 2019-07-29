package com.hkt.btu.noc.core.dao.mapper;

import com.hkt.btu.noc.core.dao.entity.NocAccessRequestEquipEntity;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NocAccessRequestEquipMapper {
    // select
    List<NocAccessRequestEquipEntity> getAccessRequestEquipsByAccessRequestId(Integer accessRequestId, Integer companyId, Integer userId);

    // paged select


    // update


    // insert
    void insertEquips(List<NocAccessRequestEquipEntity> equipEntityList);
}
