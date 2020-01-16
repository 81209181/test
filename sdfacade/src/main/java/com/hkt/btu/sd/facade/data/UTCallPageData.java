package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.time.LocalDateTime;
import java.util.List;

public class UTCallPageData implements DataInterface {
    private Integer utCallId;
    private String bsnNum;
    private String code;
    private String msg;
    private String serviceCode;
    private String seq;
    private String seqType;
    private String testStatus;
    private LocalDateTime lastCheckDate;
    private Integer ticketDetId;
    private LocalDateTime createDate;

    public Integer getUtCallId() {
        return utCallId;
    }

    public void setUtCallId(Integer utCallId) {
        this.utCallId = utCallId;
    }

    public String getBsnNum() {
        return bsnNum;
    }

    public void setBsnNum(String bsnNum) {
        this.bsnNum = bsnNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getSeqType() {
        return seqType;
    }

    public void setSeqType(String seqType) {
        this.seqType = seqType;
    }

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    public LocalDateTime getLastCheckDate() {
        return lastCheckDate;
    }

    public void setLastCheckDate(LocalDateTime lastCheckDate) {
        this.lastCheckDate = lastCheckDate;
    }

    public Integer getTicketDetId() {
        return ticketDetId;
    }

    public void setTicketDetId(Integer ticketDetId) {
        this.ticketDetId = ticketDetId;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
}