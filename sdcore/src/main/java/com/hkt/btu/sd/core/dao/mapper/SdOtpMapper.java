package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdOtpEntity;

import java.time.LocalDateTime;

public interface SdOtpMapper {

    SdOtpEntity getOtp(String otp, String action);


    void insertOtp(Integer userId, String action, String otp, LocalDateTime expirydate, Integer createby);


    void expireOtp(String otp);

    SdOtpEntity getOtpByUserId(Integer userId, String action);
}
