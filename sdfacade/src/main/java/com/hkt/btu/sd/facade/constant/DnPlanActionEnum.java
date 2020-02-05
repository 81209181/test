package com.hkt.btu.sd.facade.constant;

import org.apache.commons.lang3.StringUtils;

import static com.hkt.btu.sd.facade.data.norars.NoraDnPlanData.ACTION;


public enum DnPlanActionEnum {
    ASSIGNED (ACTION.ASSIGNED_CODE, ACTION.ASSIGNED_DESC),
    WORKING (ACTION.WORKING_CODE, ACTION.WORKING_DESC),
    CLEARED (ACTION.CLEARED_CODE, ACTION.CLEARED_DESC),
    ;


    DnPlanActionEnum(String code, String codeDesc){
        this.code = code;
        this.codeDesc = codeDesc;
    }

    public static DnPlanActionEnum getEnum(String code) {
        for(DnPlanActionEnum dnPlanActionEnum : values()){
            if(StringUtils.equals(code, dnPlanActionEnum.code)){
                return dnPlanActionEnum;
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
