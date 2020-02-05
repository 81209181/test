package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class SdResetPwdFormData implements DataInterface {
    private String resetOtp;
    private String newPassword;
    private String newPasswordRe;


    public String getResetOtp() {
        return resetOtp;
    }

    public void setResetOtp(String resetOtp) {
        this.resetOtp = resetOtp;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordRe() {
        return newPasswordRe;
    }

    public void setNewPasswordRe(String newPasswordRe) {
        this.newPasswordRe = newPasswordRe;
    }

}
