package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

public class SdServiceTypeEntity extends BaseEntity {

    public static class SERVICE_TYPE{
        public static final String UNKNOWN = "UNKNOWN";
    }
    public static class SERVICE_TYPE_NAME{
        public static final String UNKNOWN_SERVICE_TYPE = "Unknown Service Type";
    }

    private String serviceTypeCode;
    private String serviceTypeName;

    public String getServiceTypeCode() {
        return serviceTypeCode;
    }

    public void setServiceTypeCode(String serviceTypeCode) {
        this.serviceTypeCode = serviceTypeCode;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }
}
