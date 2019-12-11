package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeOfferMappingBean;

import java.util.List;

public interface SdServiceTypeService {

    List<SdServiceTypeBean> getServiceTypeList();

    List<SdServiceTypeOfferMappingBean> getServiceTypeOfferMappingBean();

    SdServiceTypeBean getServiceTypeByOfferName(String offerName);

    void reload();

    void updateServiceTypeOfferMappingByJob(List<SdServiceTypeOfferMappingBean> serviceTypeOfferMapping);

    String getServiceTypeDescByServiceTypeCode(String code);

    void reloadServiceTypeOfferMapping();

    void reloadServiceTypeList();

    void createServiceTypeOfferMapping(String serviceTypeCode, String offerName);

    void updateServiceTypeOfferMappingByUser(String oldServiceTypeCode, String serviceTypeCode,
                                             String oldOfferName, String offerName);

    SdServiceTypeOfferMappingBean getServiceTypeOfferMappingBeanByCodeAndOfferName(String oldServiceTypeCode, String oldOfferName);

    void deleteServiceTypeOfferMapping(String serviceTypeCode, String offerName);
}
