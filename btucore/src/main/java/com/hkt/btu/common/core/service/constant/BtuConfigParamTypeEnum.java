package com.hkt.btu.common.core.service.constant;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

public enum BtuConfigParamTypeEnum {
    STRING(BtuConfigParamEntity.TYPE.STRING, String.class),
    INTEGER(BtuConfigParamEntity.TYPE.INTEGER, Integer.class),
    DOUBLE(BtuConfigParamEntity.TYPE.DOUBLE, Double.class),
    BOOLEAN(BtuConfigParamEntity.TYPE.BOOLEAN, Boolean.class),
    LOCAL_DATE_TIME(BtuConfigParamEntity.TYPE.LOCAL_DATE_TIME, LocalDateTime.class),
    UNKNOWN(null, Object.class)
    ;

    private String typeCode;
    private Class clazz;

    BtuConfigParamTypeEnum(String typeCode, Class clazz) {
        this.typeCode = typeCode;
        this.clazz = clazz;
    }

    public static BtuConfigParamTypeEnum getEnum(String key) {
        for(BtuConfigParamTypeEnum e : values()){
            if(StringUtils.equals(key, e.typeCode)){
                return e;
            }
        }
        return UNKNOWN;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public Class getClazz() {
        return clazz;
    }
}
