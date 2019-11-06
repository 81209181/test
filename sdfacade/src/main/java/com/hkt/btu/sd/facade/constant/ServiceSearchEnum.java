package com.hkt.btu.sd.facade.constant;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public enum ServiceSearchEnum {
    BSN ("bsn", "BSN"),
    TENANT_ID ("tenantId", "Tenant ID"),
    DN ("dn", "DN")
    ;


    ServiceSearchEnum (String key, String keyDesc){
        this.key = key;
        this.keyDesc = keyDesc;
    }

    public static ServiceSearchEnum getEnum(String key) {
        for(ServiceSearchEnum serviceSearchEnum : values()){
            if(StringUtils.equals(key, serviceSearchEnum.key)){
                return serviceSearchEnum;
            }
        }
        return null;
    }

    private String key;
    private String keyDesc;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeyDesc() {
        return keyDesc;
    }

    public void setKeyDesc(String keyDesc) {
        this.keyDesc = keyDesc;
    }
}
