package com.hkt.btu.sd.core.service.constant;

import org.apache.commons.lang3.StringUtils;

public enum TicketTypeEnum {
    JOB("JOB", "JOB"),
    QUERY("QUERY", "QUERY");

    TicketTypeEnum (String typeCode, String typeDesc){
        this.typeCode = typeCode;
        this.typeDesc = typeDesc;
    }

    public static TicketTypeEnum getEnum(String typeCode) {
        for(TicketTypeEnum ticketTypeEnum : values()){
            if(StringUtils.equals(typeCode, ticketTypeEnum.typeCode)){
                return ticketTypeEnum;
            }
        }
        return null;
    }

    private String typeCode;
    private String typeDesc;

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }
}
