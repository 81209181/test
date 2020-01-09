package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;
import java.time.LocalDateTime;

public class UTCallRequestBean extends BaseBean{
    private Integer UTCALLID;
    private String BSNNUM;
    private String CODE;
    private String MSG;
    private String SERVICECODE;
    private String SEQ;
    private String SEQTYPE;
    private LocalDateTime LASTCHECKDATE;

    public Integer getUTCALLID() {
        return UTCALLID;
    }

    public void setUTCALLID(Integer UTCALLID) {
        this.UTCALLID = UTCALLID;
    }

    public String getBSNNUM() {
        return BSNNUM;
    }

    public void setBSNNUM(String BSNNUM) {
        this.BSNNUM = BSNNUM;
    }

    public LocalDateTime getLASTCHECKDATE() {
        return LASTCHECKDATE;
    }

    public void setLASTCHECKDATE(LocalDateTime LASTCHECKDATE) {
        this.LASTCHECKDATE = LASTCHECKDATE;
    }

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