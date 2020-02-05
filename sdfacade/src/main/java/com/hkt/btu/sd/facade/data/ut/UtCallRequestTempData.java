package com.hkt.btu.sd.facade.data.ut;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

public class UtCallRequestTempData implements DataInterface {
    private String CODE;
    private String MSG;
    private String SERVICECODE;
    private String SEQ;
    private String SEQTYPE;

    public static class DATA_KEY {
        public static final String CODE = "CODE";
        public static final String MSG = "MSG";
        public static final String SERVICECODE = "SERVICECODE";
        public static final String SEQ = "SEQ";
        public static final String SEQTYPE = "SEQTYPE";
    }

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

    public String getSERVICECODE() {
        return SERVICECODE;
    }

    public void setSERVICECODE(String SERVICECODE) {
        this.SERVICECODE = SERVICECODE;
    }

    public String getSEQ() {
        return SEQ;
    }

    public void setSEQ(String SEQ) {
        this.SEQ = SEQ;
    }

    public String getSEQTYPE() {
        return SEQTYPE;
    }

    public void setSEQTYPE(String SEQTYPE) {
        this.SEQTYPE = SEQTYPE;
    }
}