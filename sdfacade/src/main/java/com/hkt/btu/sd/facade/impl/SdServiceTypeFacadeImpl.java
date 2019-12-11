package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.SdServiceTypeService;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeOfferMappingBean;
import com.hkt.btu.sd.facade.SdServiceTypeFacade;
import com.hkt.btu.sd.facade.data.SdServiceTypeData;
import com.hkt.btu.sd.facade.data.SdServiceTypeOfferMappingData;
import com.hkt.btu.sd.facade.data.UpdateServiceTypeOfferMappingData;
import com.hkt.btu.sd.facade.populator.SdServiceTypeDataPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SdServiceTypeFacadeImpl implements SdServiceTypeFacade {
    private static final Logger LOG = LogManager.getLogger(SdServiceTypeFacadeImpl.class);


    @Resource(name = "serviceTypeService")
    SdServiceTypeService serviceTypeService;

    @Resource(name = "serviceTypeDataPopulator")
    SdServiceTypeDataPopulator serviceTypeDataPopulator;

    @Override
    public List<SdServiceTypeData> getServiceTypeList() {
        return serviceTypeService.getServiceTypeList().stream().map(bean -> {
            SdServiceTypeData data = new SdServiceTypeData();
            serviceTypeDataPopulator.populate(bean, data);
            return data;
        }).collect(Collectors.toList());
    }

    @Override
    public String getServiceTypeDescByServiceTypeCode(String code) {
        return serviceTypeService.getServiceTypeDescByServiceTypeCode(code);
    }

    @Override
    public SdServiceTypeData getServiceTypeByOfferName(String offerName) {
        SdServiceTypeData data = new SdServiceTypeData();
        SdServiceTypeBean bean = serviceTypeService.getServiceTypeByOfferName(offerName);
        serviceTypeDataPopulator.populate(bean, data);
        return data;
    }

    @Override
    public boolean needCheckPendingOrder(String serviceType) {
        if (StringUtils.isEmpty(serviceType)) {
            return false;
        }

        switch (serviceType) {
            case SdServiceTypeBean.SERVICE_TYPE.BROADBAND:
            case SdServiceTypeBean.SERVICE_TYPE.VOIP:
                return true;
            default:
                return false;
        }
    }

    @Override
    public List<SdServiceTypeOfferMappingData> getServiceTypeMappingList() {
        List<SdServiceTypeOfferMappingBean> serviceTypeOfferMappingList = serviceTypeService.getServiceTypeOfferMappingBean();
        List<SdServiceTypeBean> serviceTypeList = serviceTypeService.getServiceTypeList();
        if (CollectionUtils.isEmpty(serviceTypeOfferMappingList) || CollectionUtils.isEmpty(serviceTypeList)) {
            return null;
        }

        List<SdServiceTypeOfferMappingData> mappingDataList = new ArrayList<>();

        for (SdServiceTypeOfferMappingBean mappingBean : serviceTypeOfferMappingList) {
            for (SdServiceTypeBean serviceTypeBean : serviceTypeList) {
                if (mappingBean.getServiceTypeCode().equals(serviceTypeBean.getServiceTypeCode())) {
                    SdServiceTypeOfferMappingData data = new SdServiceTypeOfferMappingData();
                    data.setOfferName(mappingBean.getOfferName());
                    data.setServiceTypeName(serviceTypeBean.getServiceTypeName());
                    mappingDataList.add(data);
                }
            }
        }

        return mappingDataList;
    }

    @Override
    public boolean createServiceTypeOfferMapping(String serviceTypeCode, String offerName) {
        if (StringUtils.isEmpty(serviceTypeCode) || StringUtils.isEmpty(offerName)) {
            return false;
        }
        try {
            serviceTypeService.createServiceTypeOfferMapping(serviceTypeCode, offerName);
            serviceTypeService.reloadServiceTypeOfferMapping();
            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateServiceTypeOfferMapping(UpdateServiceTypeOfferMappingData data) {
        try {
            SdServiceTypeOfferMappingBean bean = serviceTypeService.
                    getServiceTypeOfferMappingBeanByCodeAndOfferName(data.getServiceTypeCode(), data.getOfferName());
            if (bean != null) {
                return false;
            }
            serviceTypeService.updateServiceTypeOfferMappingByUser(data.getOldServiceTypeCode(), data.getServiceTypeCode(),
                    data.getOldOfferName(), data.getOfferName());
            serviceTypeService.reloadServiceTypeOfferMapping();
            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteServiceTypeOfferMapping(String serviceTypeCode, String offerName) {
        if (StringUtils.isEmpty(serviceTypeCode) || StringUtils.isEmpty(offerName)) {
            return false;
        }
        try {
            serviceTypeService.deleteServiceTypeOfferMapping(serviceTypeCode, offerName);
            serviceTypeService.reloadServiceTypeOfferMapping();
            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return false;
    }
}
