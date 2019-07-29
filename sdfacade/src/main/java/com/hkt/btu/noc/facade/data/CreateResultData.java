package com.hkt.btu.noc.facade.data;

public class CreateResultData {
    private Integer newId;
    private String errorMsg;


    public static CreateResultData of(Integer newId){
        return new CreateResultData(newId, null);
    }

    public static CreateResultData of(String errorMsg){
        return new CreateResultData(null, errorMsg);
    }


    private CreateResultData(Integer newId, String errorMsg) {
        this.newId = newId;
        this.errorMsg = errorMsg;
    }

    public Integer getNewId() {
        return newId;
    }

    public void setNewId(Integer newId) {
        this.newId = newId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
