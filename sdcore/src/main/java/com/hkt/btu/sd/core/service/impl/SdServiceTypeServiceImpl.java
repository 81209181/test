package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdServiceTypeEntity;
import com.hkt.btu.sd.core.dao.mapper.SdServiceTypeMapper;
import com.hkt.btu.sd.core.service.SdServiceTypeService;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeOfferMappingBean;
import com.hkt.btu.sd.core.service.populator.SdServiceTypeBeanPopulator;
import com.hkt.btu.sd.core.service.populator.SdServiceTypeOfferMappingBeanPopulator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SdServiceTypeServiceImpl implements SdServiceTypeService {

    private static final Logger LOG = LogManager.getLogger(SdServiceTypeServiceImpl.class);

    private static List<SdServiceTypeBean> SERVICE_TYPE_LIST = null;
    private static List<SdServiceTypeOfferMappingBean> SERVICE_TYPE_OFFER_MAPPING = null;

    @Resource
    SdServiceTypeMapper serviceTypeMapper;

    @Resource(name = "serviceTypeBeanPopulator")
    SdServiceTypeBeanPopulator serviceTypeBeanPopulator;
    @Resource(name = "serviceTypeOfferMappingBeanPopulator")
    SdServiceTypeOfferMappingBeanPopulator  serviceTypeOfferMappingBeanPopulator;

    @Override
    public List<SdServiceTypeBean> getServiceTypeList() {
        return Optional.ofNullable(SERVICE_TYPE_LIST).orElseGet(() -> {
            reloadServiceTypeList();
            return SERVICE_TYPE_LIST;
        });
    }

    @Override
    public String getServiceTypeByOfferName(String offerName) {
        if (StringUtils.containsIgnoreCase(offerName, "broadband")) {
            return getServiceTypeList().stream()
                    .filter(sdServiceTypeBean -> sdServiceTypeBean.getServiceTypeCode().equals("BN"))
                    .findFirst().map(SdServiceTypeBean::getServiceTypeName).orElse(SdServiceTypeEntity.SERVICE_TYPE_NAME.UNKNOWN_SERVICE_TYPE);
        } else {
            return  Optional.ofNullable(SERVICE_TYPE_OFFER_MAPPING).orElseGet(() -> {
                reloadServiceTypeOfferMapping();
                return SERVICE_TYPE_OFFER_MAPPING;
            }).stream().filter(bean -> StringUtils.equals(offerName,bean.getOfferName()))
                    .findFirst().flatMap(bean -> getServiceTypeList().stream()
                            .filter(sdServiceTypeBean -> sdServiceTypeBean.getServiceTypeCode().equals(bean.getServiceTypeCode()))
                            .findFirst().map(SdServiceTypeBean::getServiceTypeName)
                    ).orElse(SdServiceTypeEntity.SERVICE_TYPE_NAME.UNKNOWN_SERVICE_TYPE);
        }
    }

    @Override
    public void reload() {
        reloadServiceTypeList();
        reloadServiceTypeOfferMapping();
    }

    private void reloadServiceTypeOfferMapping() {
        LOG.info("reload service type offer mapping.");
        SERVICE_TYPE_OFFER_MAPPING = serviceTypeMapper.getServiceTypeOfferMapping().stream().map(entity -> {
            SdServiceTypeOfferMappingBean bean = new SdServiceTypeOfferMappingBean();
            serviceTypeOfferMappingBeanPopulator.populate(entity,bean);
            return bean;
        }).collect(Collectors.toList());
    }


    private void reloadServiceTypeList() {
        LOG.info("reload service type list.");
        SERVICE_TYPE_LIST = serviceTypeMapper.getServiceTypeList().stream().map(entity -> {
            SdServiceTypeBean bean = new SdServiceTypeBean();
            serviceTypeBeanPopulator.populate(entity, bean);
            return bean;
        }).collect(Collectors.toList());
    }
}
