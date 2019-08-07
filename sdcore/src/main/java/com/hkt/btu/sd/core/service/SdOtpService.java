package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SdOtpBean;

public interface SdOtpService {
    SdOtpBean getValidResetPwdOtp(String otp);

    String generatePwdResetOtp(Integer userId);

    void expireOtp(String otp);

    SdOtpBean getValidResetPwdOtp(Integer userId);
}
