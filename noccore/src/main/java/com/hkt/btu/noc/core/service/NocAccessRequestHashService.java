package com.hkt.btu.noc.core.service;

public interface NocAccessRequestHashService {
    // jobs
    void generateHashId();


    Integer getHashedId(Integer accessRequestId);
    Integer getAccessRequestId(Integer hashedId);
}
