package com.hkt.btu.sd.facade.data;

public class CreateResultData {
    private String newId;
    private String errorMsg;
    private String passwordMsg;

    public static CreateResultData of(String errorMsg){
        return new CreateResultData(null, errorMsg);
    }

    public CreateResultData(String newId, String errorMsg) {
        this.newId = newId;
        this.errorMsg = errorMsg;
    }

    public CreateResultData(String newId, String errorMsg, String passwordMsg) {
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
