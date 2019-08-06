package com.hkt.btu.sd.core.dao.mapper;



public interface SdAccessRequestHashMapper {
    // select
    Integer getHashedId(Integer accessRequestId);
    Integer getAccessRequestId(Integer hashedId);
    Integer getMaxHashedId();

    // paged select
    // update

    // insert
    void insertHashedId(Integer accessRequestId, Integer hashedId);
}
