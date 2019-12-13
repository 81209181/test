package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.data.SdServiceTypeData;
import com.hkt.btu.sd.facade.data.SdServiceTypeOfferMappingData;
import com.hkt.btu.sd.facade.data.UpdateServiceTypeOfferMappingData;

import java.util.List;

public interface SdServiceTypeFacade {
    List<SdServiceTypeData> getServiceTypeList();
    SdServiceTypeData getServiceTypeByOfferName(String offerName);

    List<SdServiceTypeOfferMappingData> getServiceTypeMappingList();
    String getServiceTypeDescByServiceTypeCode(String code);

    boolean needCheckPendingOrder(String serviceType);

    boolean createServiceTypeOfferMapping(String serviceTypeCode, String offerName);
    boolean updateServiceTypeOfferMapping(UpdateServiceTypeOfferMappingData data);
    boolean deleteServiceTypeOfferMapping(String serviceTypeCode, String offerName);
}
