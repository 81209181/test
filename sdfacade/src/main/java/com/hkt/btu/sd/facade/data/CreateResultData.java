package com.hkt.btu.sd.facade.data;

public class CreateResultData {
    private String newId;
    private String errorMsg;

    public static CreateResultData of(String errorMsg){
        return new CreateResultData(null, errorMsg);
    }

    public CreateResultData(String newId, String errorMsg) {
        this.newId = newId;
        this.errorMsg = errorMsg;
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
}
