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
        target.setItsmUrl(source.getUrl());
        target.setDescription(source.getDescription());
        target.setServiceAddress(source.getServiceAddress());
        target.setGridId(source.getGridId());
        target.setExchangeBuildingId(source.getExchangeBuildingId());
        target.setRelatedBsn(source.getRelatedBsn());
    }

    public void populateFromSdTicketServiceData(SdTicketServiceData source, SdTicketServiceInfoData target) {
        target.setTicketDetId(source.getTicketDetId());
        target.setDetailButton(source.isDetailButton());
        target.setJobId(source.getJobId());
        target.setFaultsList(source.getFaultsList());
        target.setServiceType(source.getServiceType());
        target.setServiceNo(source.getServiceCode());
        target.setNgn3reset(SdServiceTypeBean.SERVICE_TYPE.VOIP.equals(source.getServiceType()));
    }
}
