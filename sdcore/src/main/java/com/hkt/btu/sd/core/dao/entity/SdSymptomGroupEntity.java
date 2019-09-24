package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

public class SdSymptomGroupEntity extends BaseEntity {

    private String symptomGroupCode;
    private String symptomGroupName;

    public String getSymptomGroupCode() {
        return symptomGroupCode;
    }

    public void setSymptomGroupCode(String symptomGroupCode) {
        this.symptomGroupCode = symptomGroupCode;
    }

    public String getSymptomGroupName() {
        return symptomGroupName;
    }

    public void setSymptomGroupName(String symptomGroupName) {
        this.symptomGroupName = symptomGroupName;
    }
}
