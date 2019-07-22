package com.hkt.btu.noc.facade.data;

public class ResetPwdFormData {
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
