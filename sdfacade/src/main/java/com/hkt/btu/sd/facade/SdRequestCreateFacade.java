package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.constant.ServiceSearchEnum;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.data.bes.BesFaultInfoData;

import java.util.List;

public interface SdRequestCreateFacade {
    List<ServiceSearchEnum> getSearchKeyEnumList();
    SdRequestCreateSearchResultsData searchProductList(String searchKey, String searchValue);

    SdTicketInfoData getTicketInfo(SdTicketMasData sdTicketMasData);

    BesFaultInfoData getCustomerInfo(String serviceCode);

    SdTicketServiceInfoData getServiceInfoInApi(String serviceNumber);
}
