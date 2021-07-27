package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

public class SdSymptomCodePerfixEntity extends BaseEntity {
    private String symptomGroupCode;
    private String symtomCodePrefix;

    public String getSymptomGroupCode() {
        return symptomGroupCode;
    }

    public void setSymptomGroupCode(String symptomGroupCode) {
        this.symptomGroupCode = symptomGroupCode;
    }

    public String getSymtomCodePrefix() {
        return symtomCodePrefix;
    }

    public void setSymtomCodePrefix(String symtomCodePrefix) {
        this.symtomCodePrefix = symtomCodePrefix;
    }
}
