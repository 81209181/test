package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

public class BesSubFaultData implements DataInterface {
    public static final BesSubFaultData NOT_FOUND = new BesSubFaultData("CD0001", "No Fault History Found");
    public static final BesSubFaultData MISSING_PARAM = new BesSubFaultData("CD0002", "Missing Parameter");
    public static final BesSubFaultData FAIL = new BesSubFaultData("CD0003", "General System Failure");

    private String msgCode;
    private String description;


    public BesSubFaultData () {
        this.msgCode = "CD0000";
        this.description = "SUCCESS";
    }
    private BesSubFaultData (String msgCode, String description) {
        this.msgCode = msgCode;
        this.description = description;
    }

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
