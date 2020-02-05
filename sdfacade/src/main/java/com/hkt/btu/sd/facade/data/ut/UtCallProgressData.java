package com.hkt.btu.sd.facade.data.ut;

import com.hkt.btu.common.facade.data.DataInterface;

public class UtCallProgressData implements DataInterface {
    private String CODE;
    private String MSG;
    private UtCallActionData ACTIONDATA;

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    public String getMSG() {
        return MSG;
    }

    public void setMSG(String MSG) {
        this.MSG = MSG;
    }

    public UtCallActionData getACTIONDATA() {
        return ACTIONDATA;
    }

    public void setACTIONDATA(UtCallActionData ACTIONDATA) {
        this.ACTIONDATA = ACTIONDATA;
    }
}