package com.hkt.btu.sd.facade.data;

import java.util.List;

public class EditResultData {

    private String errorMsg;
    private List<?> list;

    public static EditResultData error(String errorMsg) {
        EditResultData data = new EditResultData();
        data.setErrorMsg(errorMsg);
        return data;
    }

    public static EditResultData dataList(List<?> list) {
        EditResultData data = new EditResultData();
        data.setList(list);
        return data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
