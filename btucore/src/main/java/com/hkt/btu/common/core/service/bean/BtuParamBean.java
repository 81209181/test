package com.hkt.btu.common.core.service.bean;

import com.hkt.btu.common.core.service.constant.BtuConfigParamTypeEnum;

public class BtuParamBean extends BaseBean {
    private BtuConfigParamTypeEnum paramType;
    private Object value;


    public BtuConfigParamTypeEnum getParamType() {
        return paramType;
    }

    public void setParamType(BtuConfigParamTypeEnum paramType) {
        this.paramType = paramType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
