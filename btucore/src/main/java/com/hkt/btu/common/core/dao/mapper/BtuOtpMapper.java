package com.hkt.btu.common.core.dao.mapper;

import com.hkt.btu.common.core.dao.entity.BtuOtpEntity;

import java.time.LocalDateTime;

public interface BtuOtpMapper {
    BtuOtpEntity getOtp(String otp, String action);
    void insertOtp(Integer userId, String action, String otp, LocalDateTime expirydate, Integer createby);
    void expireOtp(String otp);
}
