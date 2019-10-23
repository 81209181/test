package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.constant.ServiceSearchEnum;
import com.hkt.btu.sd.facade.data.BesFaultInfoData;
import com.hkt.btu.sd.facade.data.RequestCreateSearchResultsData;
import com.hkt.btu.sd.facade.data.SdTicketInfoData;
import com.hkt.btu.sd.facade.data.SdTicketMasData;

import java.util.List;

public interface SdRequestCreateFacade {
    List<ServiceSearchEnum> getSearchKeyEnumList();
    RequestCreateSearchResultsData searchProductList(String searchKey, String searchValue);

    SdTicketInfoData getTicketInfo(SdTicketMasData sdTicketMasData);

    BesFaultInfoData getCustomerInfo(String serviceCode);
}
