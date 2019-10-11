package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.data.RequestCreateSearchResultsData;
import com.hkt.btu.sd.facade.data.SdTicketInfoData;
import com.hkt.btu.sd.facade.data.SdTicketMasData;

public interface SdRequestCreateFacade {
    RequestCreateSearchResultsData searchProductList(String searchKey, String searchValue);

    SdTicketInfoData getTicketInfo(SdTicketMasData sdTicketMasData);
}
