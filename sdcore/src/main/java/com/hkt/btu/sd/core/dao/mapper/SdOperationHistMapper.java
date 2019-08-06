package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdOperationHistEntity;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SdOperationHistMapper {
    // select
    List<SdOperationHistEntity> getOptHistListByItemTypeAndId(String itemType, String itemId);

    // paged select


    // update


    // insert
    void insertOperationHist(SdOperationHistEntity entity);

}
