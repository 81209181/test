package com.hkt.btu.noc.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class NocAccessRequestEquipData implements DataInterface {
    private Integer equipAccessId;
    private String hashedRequestId;

    private String eqBrand;
    private String eqType;
    private String eqModel;
    private String eqSerialNum;
    private String eqRackNum;
    private String eqUNum;
    private String action;


    public Integer getEquipAccessId() {
        return equipAccessId;
    }

    public void setEquipAccessId(Integer equipAccessId) {
        this.equipAccessId = equipAccessId;
    }

    public String getHashedRequestId() {
        return hashedRequestId;
    }

    public void setHashedRequestId(String hashedRequestId) {
        this.hashedRequestId = hashedRequestId;
    }

    public String getEqBrand() {
        return eqBrand;
    }

    public void setEqBrand(String eqBrand) {
        this.eqBrand = eqBrand;
    }

    public String getEqType() {
        return eqType;
    }

    public void setEqType(String eqType) {
        this.eqType = eqType;
    }

    public String getEqModel() {
        return eqModel;
    }

    public void setEqModel(String eqModel) {
        this.eqModel = eqModel;
    }

    public String getEqSerialNum() {
        return eqSerialNum;
    }

    public void setEqSerialNum(String eqSerialNum) {
        this.eqSerialNum = eqSerialNum;
    }

    public String getEqRackNum() {
        return eqRackNum;
    }

    public void setEqRackNum(String eqRackNum) {
        this.eqRackNum = eqRackNum;
    }

    public String getEqUNum() {
        return eqUNum;
    }

    public void setEqUNum(String eqUNum) {
        this.eqUNum = eqUNum;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
