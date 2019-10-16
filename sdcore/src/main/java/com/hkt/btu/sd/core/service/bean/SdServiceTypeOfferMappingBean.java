package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

public class SdServiceTypeOfferMappingBean extends BaseBean {

    private String serviceTypeCode;
    private String offerName;

    public String getServiceTypeCode() {
        return serviceTypeCode;
    }

    public void setServiceTypeCode(String serviceTypeCode) {
        this.serviceTypeCode = serviceTypeCode;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }
}
