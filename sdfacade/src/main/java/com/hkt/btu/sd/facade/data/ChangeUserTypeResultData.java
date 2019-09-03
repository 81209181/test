package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class ChangeUserTypeResultData implements DataInterface {

    private String userId;
    private String errorMsg;

    public static ChangeUserTypeResultData ofMsg(String errorMsg) {
        return new ChangeUserTypeResultData(null,errorMsg);
    }

    public static ChangeUserTypeResultData ofUser(String userId) {
        return new ChangeUserTypeResultData(userId, null);
    }


    private ChangeUserTypeResultData(String userId, String errorMsg) {
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
