package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.data.SdServiceTypeData;

import java.util.List;

public interface SdServiceTypeFacade {
    List<SdServiceTypeData> getServiceTypeList();

    String getServiceTypeByOfferName(String offerName);
}
