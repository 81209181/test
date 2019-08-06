package com.hkt.btu.sd.core.service;

public interface SdAccessRequestHashService {
    // jobs
    void generateHashId();


    Integer getHashedId(Integer accessRequestId);
    Integer getAccessRequestId(Integer hashedId);
}
