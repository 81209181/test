package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.facade.data.RequestCreateSearchResultData;
import com.hkt.btu.sd.facade.data.SdTicketServiceData;
import com.hkt.btu.sd.facade.data.SdTicketServiceInfoData;

public class SdTicketServiceInfoDataPopulator extends AbstractDataPopulator<SdTicketServiceInfoData> {

    public void populateFormRequestCreateSearchResultData(RequestCreateSearchResultData source, SdTicketServiceInfoData target) {
        target.setServiceStatus(source.getServiceStatus());
        target.setServiceStatusDesc(source.getServiceStatusDesc());
        target.setSubsId(source.getSubsId());
        target.setOfferName(source.getOfferName());
        target.setCouldUrl(source.getUrl());
        target.setDescription(source.getDescription());
        target.setServiceAddress(source.getServiceAddress());
        target.setGridId(source.getGridId());
        target.setExchangeBuildingId(source.getExchangeBuildingId());
        target.setRelatedBsn(source.getRelatedBsn());
        target.setServiceType(source.getServiceType());
        target.setServiceTypeDesc(source.getServiceTypeDesc());
    }

    public void populateFromSdTicketServiceData(SdTicketServiceData source, SdTicketServiceInfoData target) {
        target.setTicketDetId(source.getTicketDetId());
        target.setJobId(source.getJobId());
        target.setFaultsList(source.getFaultsList());
        target.setServiceNo(source.getServiceCode());
        switch (source.getServiceType()) {
            case SdServiceTypeBean.SERVICE_TYPE.ENTERPRISE_CLOUD:
            case SdServiceTypeBean.SERVICE_TYPE.ENTERPRISE_CLOUD_365:
                target.setECloudCtrl(true);
                break;
            case SdServiceTypeBean.SERVICE_TYPE.VOIP:
                target.setVoIpCtrl(true);
                break;
            case SdServiceTypeBean.SERVICE_TYPE.BROADBAND:
                target.setBnCtrl(true);
                break;

        }
    }
}
