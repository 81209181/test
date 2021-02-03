package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

public class SdCloseCodeEntity extends BaseEntity {

    private String closeCode;
    private String closeCodeDescription;

    public String getCloseCode() {
        return closeCode;
    }

    public void setCloseCode(String closeCode) {
        this.closeCode = closeCode;
    }

    public String getCloseCodeDescription() {
        return closeCodeDescription;
    }

    public void setCloseCodeDescription(String closeCodeDescription) {
        this.closeCodeDescription = closeCodeDescription;
    }
}
