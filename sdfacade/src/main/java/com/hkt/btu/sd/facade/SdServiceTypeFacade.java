package com.hkt.btu.sd.facade;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.facade.data.SdServiceTypeData;
import com.hkt.btu.sd.facade.data.SdServiceTypeOfferMappingData;
import com.hkt.btu.sd.facade.data.UpdateServiceTypeOfferMappingData;

import java.util.List;

public interface SdServiceTypeFacade {
    List<SdServiceTypeData> getServiceTypeList();

    String getServiceTypeDescByServiceTypeCode(String code);

    SdServiceTypeData getServiceTypeByOfferName(String offerName);

    boolean needCheckPendingOrder(String serviceType);

    List<SdServiceTypeOfferMappingData> getServiceTypeMappingList();

    boolean createServiceTypeOfferMapping(String serviceTypeCode, String offerName);

    boolean updateServiceTypeOfferMapping(UpdateServiceTypeOfferMappingData data);

    boolean deleteServiceTypeOfferMapping(String serviceTypeCode, String offerName);
}
