package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdOtpEntity;

import java.time.LocalDateTime;

public interface SdOtpMapper {

    SdOtpEntity getOtp(String otp, String action);


    void insertOtp(String userId, String action, String otp, LocalDateTime expirydate, String createby);


    void expireOtp(String otp);

    SdOtpEntity getOtpByUserId(String userId, String action);
}
