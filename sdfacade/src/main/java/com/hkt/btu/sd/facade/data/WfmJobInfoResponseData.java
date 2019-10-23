package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

public class WfmJobInfoResponseData implements DataInterface {

    private String primaryHandler;
    private List<WfmJobInfoData> wfmJobInfoDataList;

    public String getPrimaryHandler() {
        return primaryHandler;
    }

    public void setPrimaryHandler(String primaryHandler) {
        this.primaryHandler = primaryHandler;
    }

    public List<WfmJobInfoData> getWfmJobInfoDataList() {
        return wfmJobInfoDataList;
    }

    public void setWfmJobInfoDataList(List<WfmJobInfoData> wfmJobInfoDataList) {
        this.wfmJobInfoDataList = wfmJobInfoDataList;
    }
}
