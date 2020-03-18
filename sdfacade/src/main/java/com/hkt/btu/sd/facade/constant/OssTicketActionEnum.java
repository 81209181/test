package com.hkt.btu.sd.facade.constant;

import org.apache.commons.lang3.StringUtils;


public enum OssTicketActionEnum {
    CREATE ("Create", "Created ticket."),
    CLOSE("Close", "Closed ticket.")
    ;


    OssTicketActionEnum(String key, String keyDesc){
        this.code = key;
        this.codeDesc = keyDesc;
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
