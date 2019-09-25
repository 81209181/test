package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdServiceTypeEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomMappingEntity;
import com.hkt.btu.sd.core.dao.mapper.SdSymptomMapper;
import com.hkt.btu.sd.core.service.SdSymptomService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomMappingBean;
import com.hkt.btu.sd.core.service.populator.SdServiceTypeBeanPopulator;
import com.hkt.btu.sd.core.service.populator.SdSymptomBeanPopulator;
import com.hkt.btu.sd.core.service.populator.SdSymptomMappingBeanPopulator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class SdSymptomServiceImpl implements SdSymptomService {
    private static final Logger LOG = LogManager.getLogger(SdSymptomServiceImpl.class);

    @Resource
    private SdSymptomMapper sdSymptomMapper;

    @Resource(name = "userService")
    SdUserService userService;

    @Resource(name = "symptmBeanPopulator")
    SdSymptomBeanPopulator symptomBeanPopulator;
    @Resource(name = "serviceTypeBeanPopulator")
    SdServiceTypeBeanPopulator serviceTypeBeanPopulator;
    @Resource(name = "symptmMappingBeanPopulator")
    SdSymptomMappingBeanPopulator symptomMappingBeanPopulator;

    @Override
    public List<SdSymptomBean> getSymptomGroupList() {
        List<SdSymptomEntity> entityList = sdSymptomMapper.getSymptomGroupList();
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        List<SdSymptomBean> beanList = new LinkedList<>();
        for (SdSymptomEntity entity : entityList) {
            SdSymptomBean bean = new SdSymptomBean();
            symptomBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }

        return beanList;
    }

    @Override
    public void createSymptom(String symptomCode, String symptomGroupCode, String symptomDescription) {
        String createby = userService.getCurrentUserUserId();
        sdSymptomMapper.createSymptom(symptomCode, symptomGroupCode, symptomDescription, createby);
        LOG.info(String.format("Created symptom %s - %s", symptomCode, symptomDescription));
    }

    @Override
    public List<SdServiceTypeBean> getServiceTypeList() {
        List<SdServiceTypeEntity> entityList = sdSymptomMapper.getServiceTypeList();
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<SdServiceTypeBean> beanList = new LinkedList<>();
        for (SdServiceTypeEntity entity : entityList) {
            SdServiceTypeBean bean = new SdServiceTypeBean();
            serviceTypeBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }

        return beanList;
    }

    @Override
    public List<SdSymptomBean> getAllSymptom() {
        List<SdSymptomEntity> entityList = sdSymptomMapper.getAllSymptom();
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<SdSymptomBean> beanList = new LinkedList<>();
        for (SdSymptomEntity entity : entityList) {
            SdSymptomBean bean = new SdSymptomBean();
            symptomBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }

        return beanList;
    }

    @Override
    public SdSymptomBean getSymptomBySymptomCode(String symptomCode) {
        SdSymptomEntity entity = sdSymptomMapper.getSymptomBySymptomCode(symptomCode);
        if (entity == null) {
            return null;
        }
        SdSymptomBean bean = new SdSymptomBean();
        symptomBeanPopulator.populate(entity, bean);
        return bean;
    }

    @Override
    public void deleteSymptomMapping(String symptomCode) {
        sdSymptomMapper.deleteSymptomMapping(symptomCode);
    }

    @Override
    public void createSymptomMapping(String serviceTypeCode, String symptomCode) {
        String createby = userService.getCurrentUserUserId();
        sdSymptomMapper.createSymptomMapping(serviceTypeCode, symptomCode, createby);
    }

    @Override
    public List<SdSymptomMappingBean> getSymptomMapping(String symptomCode) {
        List<SdSymptomMappingBean> results = new LinkedList<>();

        List<SdSymptomMappingEntity> symptomMappingEntity = sdSymptomMapper.getSymptomMapping(symptomCode);

        if (CollectionUtils.isEmpty(symptomMappingEntity)) {
            return null;
        }
        for (SdSymptomMappingEntity entity : symptomMappingEntity) {
            SdSymptomMappingBean bean = new SdSymptomMappingBean();
            symptomMappingBeanPopulator.populate(entity, bean);
            results.add(bean);
        }
        return results;
    }
}
