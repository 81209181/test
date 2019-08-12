package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SdOtpBean;

public interface SdOtpService {
    SdOtpBean getValidResetPwdOtp(String otp);

    String generatePwdResetOtp(String userId);

    void expireOtp(String otp);

    SdOtpBean getValidPwdOtp(String userId, String action);

    String generatePwdOtp(String userId, String action);
}
