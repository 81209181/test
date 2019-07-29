package com.hkt.btu.noc.core.service;

import com.hkt.btu.noc.core.service.bean.NocOtpBean;

public interface NocOtpService {
    NocOtpBean getValidResetPwdOtp(String otp);

    String generatePwdResetOtp(Integer userId);

    void expireOtp(String otp);

}
