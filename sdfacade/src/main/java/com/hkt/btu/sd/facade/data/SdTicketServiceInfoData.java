package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

public class SdTicketServiceInfoData implements DataInterface {

    private String serviceType;
    private String serviceTypeDesc;
    private String serviceNo;
    private Long subsId;
    private String offerName;
    private String serviceStatus;
    private String serviceStatusDesc;
    private String serviceAddress;
    private String pendingOrder;
    private String supplementaryOffer;
    private String offerDetail;
    private String description;
    private String couldUrl;
    private String gridId;
    private String exchangeBuildingId;
    private String relatedBsn;
    private List<SdSymptomData> faultsList;
    private Integer ticketDetId;
    private String jobId;


    private boolean bnCtrl;
    private boolean voIpCtrl;
    private boolean eCloudCtrl;

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceTypeDesc() {
        return serviceTypeDesc;
    }

    public void setServiceTypeDesc(String serviceTypeDesc) {
        this.serviceTypeDesc = serviceTypeDesc;
    }

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

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

    public String getPendingOrder() {
        return pendingOrder;
    }

    public void setPendingOrder(String pendingOrder) {
        this.pendingOrder = pendingOrder;
    }

    public String getSupplementaryOffer() {
        return supplementaryOffer;
    }

    public void setSupplementaryOffer(String supplementaryOffer) {
        this.supplementaryOffer = supplementaryOffer;
    }

    public String getOfferDetail() {
        return offerDetail;
    }

    public void setOfferDetail(String offerDetail) {
        this.offerDetail = offerDetail;
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

    public SdTicketServiceInfoData setCouldUrl(String couldUrl) {
        this.couldUrl = couldUrl;
        return this;
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
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

    public List<SdSymptomData> getFaultsList() {
        return faultsList;
    }

    public void setFaultsList(List<SdSymptomData> faultsList) {
        this.faultsList = faultsList;
    }

    public Integer getTicketDetId() {
        return ticketDetId;
    }

    public void setTicketDetId(Integer ticketDetId) {
        this.ticketDetId = ticketDetId;
    }


    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public boolean isBnCtrl() {
        return bnCtrl;
    }

    public void setBnCtrl(boolean bnCtrl) {
        this.bnCtrl = bnCtrl;
    }

    public boolean isVoIpCtrl() {
        return voIpCtrl;
    }

    public void setVoIpCtrl(boolean voIpCtrl) {
        this.voIpCtrl = voIpCtrl;
    }

    public boolean iseCloudCtrl() {
        return eCloudCtrl;
    }

    public void setECloudCtrl(boolean eCloudCtrl) {
        this.eCloudCtrl = eCloudCtrl;
    }
}
