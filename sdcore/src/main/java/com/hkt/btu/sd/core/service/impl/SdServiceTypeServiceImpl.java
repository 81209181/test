package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdServiceTypeEntity;
import com.hkt.btu.sd.core.dao.entity.SdServiceTypeOfferMappingEntity;
import com.hkt.btu.sd.core.dao.mapper.SdServiceTypeMapper;
import com.hkt.btu.sd.core.service.SdCacheService;
import com.hkt.btu.sd.core.service.SdServiceTypeService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeOfferMappingBean;
import com.hkt.btu.sd.core.service.constant.SdCacheEnum;
import com.hkt.btu.sd.core.service.populator.SdServiceTypeBeanPopulator;
import com.hkt.btu.sd.core.service.populator.SdServiceTypeOfferMappingBeanPopulator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

public class SdServiceTypeServiceImpl implements SdServiceTypeService {

    private static final Logger LOG = LogManager.getLogger(SdServiceTypeServiceImpl.class);

    private static final String CREATE_BY_SYSTEM = "system";

    @Resource
    SdServiceTypeMapper serviceTypeMapper;

    @Resource(name = "userService")
    SdUserService userService;
    @Resource(name = "cacheService")
    SdCacheService cacheService;

    @Resource(name = "serviceTypeBeanPopulator")
    SdServiceTypeBeanPopulator serviceTypeBeanPopulator;
    @Resource(name = "serviceTypeOfferMappingBeanPopulator")
    SdServiceTypeOfferMappingBeanPopulator serviceTypeOfferMappingBeanPopulator;

    @Override
    public List<SdServiceTypeBean> getServiceTypeList() {
        return (List<SdServiceTypeBean>) cacheService.getCachedObjectByCacheName(SdCacheEnum.SERVICE_TYPE_LIST.getCacheName());
    }

    @Override
    public List<SdServiceTypeOfferMappingBean> getServiceTypeOfferMappingBean() {
        return (List<SdServiceTypeOfferMappingBean>) cacheService.getCachedObjectByCacheName(SdCacheEnum.SERVICE_TYPE_OFFER_MAPPING.getCacheName());
    }

    @Override
    public SdServiceTypeBean getServiceTypeByOfferName(String offerName) {
        return getServiceTypeOfferMappingBean().stream().filter(bean -> StringUtils.equalsIgnoreCase(offerName, bean.getOfferName()))
                .findFirst().flatMap(bean -> getServiceTypeList().stream()
                        .filter(sdServiceTypeBean -> StringUtils.equals(sdServiceTypeBean.getServiceTypeCode(), bean.getServiceTypeCode())).findFirst()
                ).orElseGet(() -> new SdServiceTypeBean(SdServiceTypeEntity.SERVICE_TYPE.UNKNOWN, SdServiceTypeEntity.SERVICE_TYPE_NAME.UNKNOWN_SERVICE_TYPE));
    }

    @Override
    public void reload() {
        LOG.info("reload service type list.");
        cacheService.reloadCachedObject(SdCacheEnum.SERVICE_TYPE_LIST.getCacheName());
        LOG.info("reload service type offer mapping.");
        cacheService.reloadCachedObject(SdCacheEnum.SERVICE_TYPE_OFFER_MAPPING.getCacheName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateServiceTypeOfferMappingByJob(List<SdServiceTypeOfferMappingBean> serviceTypeOfferMapping) {
        serviceTypeMapper.removeAll();
        serviceTypeOfferMapping.forEach(bean -> serviceTypeMapper.insertServiceTypeOfferMapping(bean.getServiceTypeCode(), bean.getOfferName(), CREATE_BY_SYSTEM));
        reload();
    }

    @Override
    public String getServiceTypeDescByServiceTypeCode(String code) {
        return getServiceTypeList().stream().filter(sdServiceTypeBean -> sdServiceTypeBean.getServiceTypeCode().equals(code))
                .map(SdServiceTypeBean::getServiceTypeName).findFirst().orElse(SdServiceTypeEntity.SERVICE_TYPE_NAME.UNKNOWN_SERVICE_TYPE);
    }

    @Override
    public List<SdServiceTypeOfferMappingBean> loadServiceTypeOfferMapping() {
        LOG.info("load service type offer mapping.");
        return serviceTypeMapper.getServiceTypeOfferMapping().stream().map(entity -> {
            SdServiceTypeOfferMappingBean bean = new SdServiceTypeOfferMappingBean();
            serviceTypeOfferMappingBeanPopulator.populate(entity, bean);
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SdServiceTypeBean> loadServiceTypeList() {
        LOG.info("load service type list.");
        return serviceTypeMapper.getServiceTypeList().stream().map(entity -> {
            SdServiceTypeBean bean = new SdServiceTypeBean();
            serviceTypeBeanPopulator.populate(entity, bean);
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createServiceTypeOfferMapping(String serviceTypeCode, String offerName) {
        String createby = userService.getCurrentSdUserBean().getUserId();
        serviceTypeMapper.insertServiceTypeOfferMapping(serviceTypeCode, offerName, createby);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateServiceTypeOfferMappingByUser(String oldServiceTypeCode, String serviceTypeCode,
                                                    String oldOfferName, String offerName) {
        String modifyby = userService.getCurrentSdUserBean().getUserId();
        serviceTypeMapper.updateServiceTypeMappingByCodeAndOfferName(oldServiceTypeCode, serviceTypeCode, oldOfferName, offerName, modifyby);
        LOG.info("update service type code in SERVICE_TYPE_OFFER_MAPPING:" + oldServiceTypeCode + " => " + serviceTypeCode + ", modify by :" + modifyby);
        LOG.info("update offerName in SERVICE_TYPE_OFFER_MAPPING:" + oldOfferName + " => " + offerName + ", modify by :" + modifyby);
    }

    @Override
    public SdServiceTypeOfferMappingBean getServiceTypeOfferMappingBeanByCodeAndOfferName(String oldServiceTypeCode, String oldOfferName) {
        SdServiceTypeOfferMappingEntity entity = serviceTypeMapper.getServiceTypeOfferMappingByCodeAndOfferName(oldServiceTypeCode, oldOfferName);
        if (entity == null) {
            return null;
        }
        SdServiceTypeOfferMappingBean bean = new SdServiceTypeOfferMappingBean();
        serviceTypeOfferMappingBeanPopulator.populate(entity, bean);
        return bean;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteServiceTypeOfferMapping(String serviceTypeCode, String offerName) {
        serviceTypeMapper.deleteServiceTypeOfferMapping(serviceTypeCode, offerName);
    }
}
