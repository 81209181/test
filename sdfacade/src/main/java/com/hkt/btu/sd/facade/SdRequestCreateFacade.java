package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.constant.ServiceSearchEnum;
import com.hkt.btu.sd.facade.data.*;

import java.util.List;

public interface SdRequestCreateFacade {
    List<ServiceSearchEnum> getSearchKeyEnumList();
    RequestCreateSearchResultsData searchProductList(String searchKey, String searchValue);

    SdTicketInfoData getTicketInfo(SdTicketMasData sdTicketMasData);

    BesFaultInfoData getCustomerInfo(String serviceCode);

    List<SdTicketServiceInfoData> getServiceInfoInApi(List<SdTicketServiceData> serviceInfo, SdTicketMasData ticketMasData);
}
