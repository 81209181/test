package com.hkt.btu.noc.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

public class NocAccessRequestEquipEntity extends BaseEntity {
    private Integer equipAccessId;
    private Integer accessRequestId;
    private Integer hashedRequestId;

    private String equipBrand;
    private String equipType;
    private String equipModel;
    private String equipSerial;

    private String equipRackNum;
    private String equipUNum;
    private String action;

    public Integer getEquipAccessId() {
        return equipAccessId;
    }

    public void setEquipAccessId(Integer equipAccessId) {
        this.equipAccessId = equipAccessId;
    }

    public Integer getAccessRequestId() {
        return accessRequestId;
    }

    public void setAccessRequestId(Integer accessRequestId) {
        this.accessRequestId = accessRequestId;
    }

    public Integer getHashedRequestId() {
        return hashedRequestId;
    }

    public void setHashedRequestId(Integer hashedRequestId) {
        this.hashedRequestId = hashedRequestId;
    }

    public String getEquipBrand() {
        return equipBrand;
    }

    public void setEquipBrand(String equipBrand) {
        this.equipBrand = equipBrand;
    }

    public String getEquipType() {
        return equipType;
    }

    public void setEquipType(String equipType) {
        this.equipType = equipType;
    }

    public String getEquipModel() {
        return equipModel;
    }

    public void setEquipModel(String equipModel) {
        this.equipModel = equipModel;
    }

    public String getEquipSerial() {
        return equipSerial;
    }

    public void setEquipSerial(String equipSerial) {
        this.equipSerial = equipSerial;
    }

    public String getEquipRackNum() {
        return equipRackNum;
    }

    public void setEquipRackNum(String equipRackNum) {
        this.equipRackNum = equipRackNum;
    }

    public String getEquipUNum() {
        return equipUNum;
    }

    public void setEquipUNum(String equipUNum) {
        this.equipUNum = equipUNum;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
