package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.facade.data.SdRequestCreateSearchResultData;
import com.hkt.btu.sd.facade.data.SdTicketServiceInfoData;

public class SdTicketServiceInfoDataPopulator extends AbstractDataPopulator<SdTicketServiceInfoData> {

    public void populateFormRequestCreateSearchResultData(SdRequestCreateSearchResultData source, SdTicketServiceInfoData target) {
        target.setServiceStatus(source.getServiceStatus());
        target.setServiceStatusDesc(source.getServiceStatusDesc());
        target.setSubsId(source.getSubsId());
        target.setOfferName(source.getOfferName());
        target.setCouldUrl(source.getUrl());
        target.setDescription(source.getDescription());
        target.setServiceAddress(source.getServiceAddress());
        target.setExchangeBuildingId(source.getExchangeBuildingId());
        target.setRelatedBsn(source.getRelatedBsn());
    }

}
