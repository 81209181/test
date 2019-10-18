package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeOfferMappingBean;

import java.util.List;

public interface SdServiceTypeService {

    List<SdServiceTypeBean> getServiceTypeList();

    String getServiceTypeByOfferName(String offerName);

    void reload();

    void updateServiceTypeOfferMapping(List<SdServiceTypeOfferMappingBean> serviceTypeOfferMapping);
}
