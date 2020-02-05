package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class SdUpdatePwdFormData implements DataInterface {
    private String oldPassword;
    private String newPassword;
    private String newPasswordRe;


    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
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
