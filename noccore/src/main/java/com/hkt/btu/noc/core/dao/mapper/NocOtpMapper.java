package com.hkt.btu.noc.core.dao.mapper;

import com.hkt.btu.noc.core.dao.entity.NocOtpEntity;

import java.time.LocalDateTime;

public interface NocOtpMapper {

    NocOtpEntity getOtp(String otp, String action);


    void insertOtp(Integer userId, String action, String otp, LocalDateTime expirydate, Integer createby);


    void expireOtp(String otp);

}
