package com.hkt.btu.noc.core.dao.mapper;



public interface NocAccessRequestHashMapper {
    // select
    Integer getHashedId(Integer accessRequestId);
    Integer getAccessRequestId(Integer hashedId);
    Integer getMaxHashedId();

    // paged select
    // update

    // insert
    void insertHashedId(Integer accessRequestId, Integer hashedId);
}
