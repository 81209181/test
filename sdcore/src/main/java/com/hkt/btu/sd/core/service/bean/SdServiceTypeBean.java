package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

public class SdServiceTypeBean extends BaseBean {

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
