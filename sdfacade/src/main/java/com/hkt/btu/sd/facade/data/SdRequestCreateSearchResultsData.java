package com.hkt.btu.sd.facade.data;

import java.util.List;

public class SdRequestCreateSearchResultsData {

    private List<SdRequestCreateSearchResultData> list;
    private String errorMsg;
    private String warningMsg;



    public List<SdRequestCreateSearchResultData> getList() {
        return list;
    }

    public void setList(List<SdRequestCreateSearchResultData> list) {
        this.list = list;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getWarningMsg() {
        return warningMsg;
    }

    public void setWarningMsg(String warningMsg) {
        this.warningMsg = warningMsg;
    }
}
