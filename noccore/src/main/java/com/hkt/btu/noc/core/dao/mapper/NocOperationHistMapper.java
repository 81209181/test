package com.hkt.btu.noc.core.dao.mapper;

import com.hkt.btu.noc.core.dao.entity.NocOperationHistEntity;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NocOperationHistMapper {
    // select
    List<NocOperationHistEntity> getOptHistListByItemTypeAndId(String itemType, String itemId);

    // paged select


    // update


    // insert
    void insertOperationHist(NocOperationHistEntity entity);

}
