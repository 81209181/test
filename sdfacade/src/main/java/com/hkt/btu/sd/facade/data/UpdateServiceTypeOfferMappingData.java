package com.hkt.btu.sd.facade.data;

import javax.validation.constraints.NotEmpty;

public class UpdateServiceTypeOfferMappingData {

    @NotEmpty
    private String offerName;
    @NotEmpty
    private String oldOfferName;
    @NotEmpty
    private String serviceTypeCode;
    @NotEmpty
    private String oldServiceTypeCode;

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOldOfferName() {
        return oldOfferName;
    }

    public void setOldOfferName(String oldOfferName) {
        this.oldOfferName = oldOfferName;
    }

    public String getServiceTypeCode() {
        return serviceTypeCode;
    }

    public void setServiceTypeCode(String serviceTypeCode) {
        this.serviceTypeCode = serviceTypeCode;
    }

    public String getOldServiceTypeCode() {
        return oldServiceTypeCode;
    }

    public void setOldServiceTypeCode(String oldServiceTypeCode) {
        this.oldServiceTypeCode = oldServiceTypeCode;
    }
}
