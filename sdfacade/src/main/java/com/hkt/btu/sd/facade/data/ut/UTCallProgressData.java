package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

public class UTCallProgressData implements DataInterface {
    private String CODE;
    private String MSG;
    private UTCallActionData ACTIONDATA;

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

    public UTCallActionData getACTIONDATA() {
        return ACTIONDATA;
    }

    public void setACTIONDATA(UTCallActionData ACTIONDATA) {
        this.ACTIONDATA = ACTIONDATA;
    }
}