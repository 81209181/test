package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

public class BesSubFaultData implements DataInterface {

    private String msgCode;
    private String description;

    private List<BesFaultInfoData> list;

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BesFaultInfoData> getList() {
        return list;
    }

    public void setList(List<BesFaultInfoData> list) {
        this.list = list;
    }
}
