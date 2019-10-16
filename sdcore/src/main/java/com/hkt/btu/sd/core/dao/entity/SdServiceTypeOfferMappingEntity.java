package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

public class SdServiceTypeOfferMappingEntity extends BaseEntity {

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
