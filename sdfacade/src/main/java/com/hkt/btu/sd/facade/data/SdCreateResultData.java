package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class SdCreateResultData implements DataInterface {

    private String newId;
    private String errorMsg;
    private String passwordMsg;

    public static SdCreateResultData of(String errorMsg) {
        return new SdCreateResultData(null, errorMsg);
    }

    public SdCreateResultData(String newId, String errorMsg) {
        this.newId = newId;
        this.errorMsg = errorMsg;
    }

    public SdCreateResultData(String newId, String errorMsg, String passwordMsg, String redirectPath) {
        this.newId = newId;
        this.errorMsg = errorMsg;
        this.passwordMsg = passwordMsg;
    }

    public String getNewId() {
        return newId;
    }

    public void setNewId(String newId) {
        this.newId = newId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getPasswordMsg() {
        return passwordMsg;
    }

    public void setPasswordMsg(String passwordMsg) {
        this.passwordMsg = passwordMsg;
    }
}
