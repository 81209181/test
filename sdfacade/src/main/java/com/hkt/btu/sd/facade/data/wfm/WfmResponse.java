package com.hkt.btu.sd.facade.data.wfm;

public class WfmResponse {

    // error
    private String code;
    private String errorMsg;

    // success
    private WfmSuccess data;
    private String type;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public WfmSuccess getData() {
        return data;
    }

    public void setData(WfmSuccess data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
