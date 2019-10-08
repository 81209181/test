package com.hkt.btu.sd.facade.data;

import com.google.gson.reflect.TypeToken;
import com.hkt.btu.common.facade.data.DataInterface;

public class WfmResponseData<WfmData extends DataInterface> implements DataInterface{
    // successful response
    private String type;

    // failure response
    private String code;
    private String errorMsg;

    private WfmData data;
    private TypeToken<WfmResponseData<WfmData>> typeToken;

    public WfmResponseData(TypeToken<WfmResponseData<WfmData>> typeToken) {
        this.typeToken = typeToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public WfmData getData() {
        return data;
    }

    public void setData(WfmData data) {
        this.data = data;
    }

    public TypeToken<WfmResponseData<WfmData>> getTypeToken() {
        return typeToken;
    }

    public void setTypeToken(TypeToken<WfmResponseData<WfmData>> typeToken) {
        this.typeToken = typeToken;
    }
}
