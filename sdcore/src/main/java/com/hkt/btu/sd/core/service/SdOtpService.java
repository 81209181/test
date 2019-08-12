package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SdOtpBean;

public interface SdOtpService {
    SdOtpBean getValidOtp(String otp);

    void expireOtp(String otp);

    SdOtpBean getValidOtp(String userId, String action);

    String generateOtp(String userId, String action);
}
