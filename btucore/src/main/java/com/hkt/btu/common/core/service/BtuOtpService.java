package com.hkt.btu.common.core.service;

import com.hkt.btu.common.core.service.bean.BtuOtpBean;

public interface BtuOtpService {
    BtuOtpBean getValidResetPwdOtp(String otp);

    String generatePwdResetOtp(Integer userId);

    void expireOtp(String otp);

}
