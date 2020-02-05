package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class SdChangeUserTypeResultData implements DataInterface {

    private String userId;
    private String errorMsg;

    public static SdChangeUserTypeResultData ofMsg(String errorMsg) {
        return new SdChangeUserTypeResultData(null,errorMsg);
    }

    public static SdChangeUserTypeResultData ofUser(String userId) {
        return new SdChangeUserTypeResultData(userId, null);
    }


    private SdChangeUserTypeResultData(String userId, String errorMsg) {
        this.userId = userId;
        this.errorMsg = errorMsg;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
