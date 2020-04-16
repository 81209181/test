package com.hkt.btu.sd.facade.constant;

import org.apache.commons.lang3.StringUtils;


public enum OssTicketActionEnum {
    CREATE ("Create", "Created ticket."),
    ARRIVAL ("Arrival", "Field Arrived."),
    CLOSE("Close", "Closed ticket.")
    ;


    OssTicketActionEnum(String code, String codeDesc){
        this.code = code;
        this.codeDesc = codeDesc;
    }

    public static OssTicketActionEnum getEnum(String key) {
        for(OssTicketActionEnum serviceSearchEnum : values()){
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
