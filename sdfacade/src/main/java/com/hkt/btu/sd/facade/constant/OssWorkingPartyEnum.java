package com.hkt.btu.sd.facade.constant;

import org.apache.commons.lang3.StringUtils;


public enum OssWorkingPartyEnum {
    PND ("PND", "PND"),
    FIELD("Field", "WFM Field Service")
    ;


    OssWorkingPartyEnum(String code, String codeDesc){
        this.code = code;
        this.codeDesc = codeDesc;
    }

    public static OssWorkingPartyEnum getEnum(String key) {
        for(OssWorkingPartyEnum serviceSearchEnum : values()){
            if(StringUtils.equals(key, serviceSearchEnum.code)){
                return serviceSearchEnum;
            }
        }
        return null;
    }

    private String code;
    private String codeDesc;

    public String getCode() {
        return code;
    }

    public String getCodeDesc() {
        return codeDesc;
    }
}
