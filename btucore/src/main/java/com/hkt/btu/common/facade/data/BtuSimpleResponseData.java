package com.hkt.btu.common.facade.data;

public class BtuSimpleResponseData implements DataInterface {
    private Boolean success;
    private String id;
    private String errorMsg;

    private BtuSimpleResponseData(Boolean success, String id, String errorMsg) {
        this.success = success;
        this.id = id;
        this.errorMsg = errorMsg;
    }

    public static BtuSimpleResponseData of(Boolean success, String id, String errorMsg){
        return new BtuSimpleResponseData(success, id, errorMsg);
    }


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
