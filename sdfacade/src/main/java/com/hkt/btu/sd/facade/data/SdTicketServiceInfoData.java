package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class SdTicketServiceInfoData implements DataInterface {

    private Long subsId;
    private String offerName;
    private String serviceStatus;
    private String serviceStatusDesc;
    private String serviceAddress;
    private String description;
    private String couldUrl;
    private String exchangeBuildingId;
    private String relatedBsn;

    public Long getSubsId() {
        return subsId;
    }

    public void setSubsId(Long subsId) {
        this.subsId = subsId;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getServiceStatusDesc() {
        return serviceStatusDesc;
    }

    public void setServiceStatusDesc(String serviceStatusDesc) {
        this.serviceStatusDesc = serviceStatusDesc;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCouldUrl() {
        return couldUrl;
    }

    public void setCouldUrl(String couldUrl) {
        this.couldUrl = couldUrl;
    }

    public String getExchangeBuildingId() {
        return exchangeBuildingId;
    }

    public void setExchangeBuildingId(String exchangeBuildingId) {
        this.exchangeBuildingId = exchangeBuildingId;
    }

    public String getRelatedBsn() {
        return relatedBsn;
    }

    public void setRelatedBsn(String relatedBsn) {
        this.relatedBsn = relatedBsn;
    }
}
