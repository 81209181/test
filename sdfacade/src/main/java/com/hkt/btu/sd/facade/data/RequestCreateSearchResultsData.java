package com.hkt.btu.sd.facade.data;

import java.util.List;

public class RequestCreateSearchResultsData {

    private List<RequestCreateSearchResultData> list;
    private String errorMsg;



    public List<RequestCreateSearchResultData> getList() {
        return list;
    }

    public void setList(List<RequestCreateSearchResultData> list) {
        this.list = list;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
