package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

public class SdServiceTypeBean extends BaseBean {

    public class SERVICE_TYPE{
        public static final String BROADBAND = "BN";
        public static final String VOIP = "VOIP";
        public static final String ENTERPRISE_CLOUD_365 = "EC_365";
        public static final String ENTERPRISE_CLOUD  = "E_CLOUD";
        public static final String UNKNOWN = "UNKNOWN";
    }

    private String serviceTypeCode;
    private String serviceTypeName;

    public SdServiceTypeBean() {
    }

    public SdServiceTypeBean(String serviceTypeCode, String serviceTypeName) {
        this.serviceTypeCode = serviceTypeCode;
        this.serviceTypeName = serviceTypeName;
    }

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
