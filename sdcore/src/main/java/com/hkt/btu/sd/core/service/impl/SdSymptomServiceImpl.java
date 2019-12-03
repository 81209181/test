package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdServiceTypeEntity;
import com.hkt.btu.sd.core.dao.entity.SdSortEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomMappingEntity;
import com.hkt.btu.sd.core.dao.mapper.SdSymptomMapper;
import com.hkt.btu.sd.core.service.SdSymptomService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdSortBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomMappingBean;
import com.hkt.btu.sd.core.service.populator.SdSymptomBeanPopulator;
import com.hkt.btu.sd.core.service.populator.SdSymptomMappingBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SdSymptomServiceImpl implements SdSymptomService {
    private static final Logger LOG = LogManager.getLogger(SdSymptomServiceImpl.class);

    @Resource
    private SdSymptomMapper sdSymptomMapper;

    @Resource(name = "userService")
    SdUserService userService;

    @Resource(name = "symptmBeanPopulator")
    SdSymptomBeanPopulator symptomBeanPopulator;
    @Resource(name = "symptmMappingBeanPopulator")
    SdSymptomMappingBeanPopulator symptomMappingBeanPopulator;

    @Override
    public List<SdSymptomBean> getSymptomGroupList() {
        List<SdSymptomEntity> entityList = sdSymptomMapper.getSymptomGroupList();
        return buildSymptomBeanList(entityList);
    }

    @Override
    public void createSymptom(String symptomCode, String symptomGroupCode, String symptomDescription) {
        String createby = userService.getCurrentUserUserId();
        sdSymptomMapper.createSymptom(symptomCode, symptomGroupCode, symptomDescription, createby);
        LOG.info(String.format("Created symptom %s - %s", symptomCode, symptomDescription));
    }

    @Override
    public Page<SdSymptomBean> searchSymptomList(Pageable pageable, String symptomGroupCode, String symptomDescription, List<SdSortBean> sortList) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        List<SdSortEntity> sortEntityList = sortList.stream().map(bean -> {
            SdSortEntity entity = new SdSortEntity();
            entity.setDir(bean.getDir());
            entity.setColumn(bean.getColumn());
            return entity;
        }).collect(Collectors.toList());

        List<SdSymptomEntity> entityList = sdSymptomMapper.searchSymptomList(offset, pageSize, symptomGroupCode, symptomDescription, sortEntityList);
        Integer totalCount = sdSymptomMapper.searchSymptomCount(symptomGroupCode, symptomDescription);

        return new PageImpl<>(buildSymptomBeanList(entityList), pageable, totalCount);
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

    @Override
    public void updateSymptom(String oldSymptomCode, String symptomCode, String symptomGroupCode, String symptomDescription) {
        String createby = userService.getCurrentUserUserId();
        sdSymptomMapper.updateSymptom(oldSymptomCode, symptomCode, symptomGroupCode, symptomDescription, createby);
        LOG.info(String.format("Updated symptom %s - %s", symptomCode, symptomDescription));
    }

    @Override
    public void editSymptomMapping(String oldSymptomCode, String symptomCode, List<String> serviceTypeList) {
        String createby = userService.getCurrentUserUserId();

        // filter unknown service type
        List<String> filteredServiceTypeList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(serviceTypeList)){
            for (String serviceType : serviceTypeList) {
                if (!StringUtils.equals(SdServiceTypeEntity.SERVICE_TYPE.UNKNOWN, serviceType)) {
                    filteredServiceTypeList.add(serviceType);
                }
            }
        }

        // get all existing service type list of the symptom
        List<SdSymptomMappingEntity> existSymptomList = sdSymptomMapper.getSymptomMapping(oldSymptomCode);
        if (CollectionUtils.isEmpty(existSymptomList)) {
            if (!CollectionUtils.isEmpty(filteredServiceTypeList)) {
                LOG.info("Created symptomCode:" + symptomCode + ", serviceTypeList:" + filteredServiceTypeList);
                sdSymptomMapper.createSymptomMapping(filteredServiceTypeList, symptomCode, createby);
            }
        } else {
            List<String> existServiceTypeList = new ArrayList<>();
            for (SdSymptomMappingEntity symptomMappingEntity : existSymptomList) {
                existServiceTypeList.add(symptomMappingEntity.getServiceTypeCode());
            }

            // find which service type to delete
            List<String> toDeleteServiceTypeList = new ArrayList<>(existServiceTypeList);
            toDeleteServiceTypeList.removeAll(filteredServiceTypeList);
            LOG.info("Deleted symptomCode:" + symptomCode + ", toDeleteServiceTypeList:" + toDeleteServiceTypeList);

            // find which service type to insert
            List<String> toInsertServiceTypeList = new ArrayList<>(filteredServiceTypeList);
            toInsertServiceTypeList.removeAll(existServiceTypeList);
            LOG.info("Created symptomCode:" + symptomCode + ", toInsertServiceTypeList:" + toInsertServiceTypeList);

            // delete service type
            if (!CollectionUtils.isEmpty(toDeleteServiceTypeList)) {
                sdSymptomMapper.deleteSymptomMapping(oldSymptomCode, toDeleteServiceTypeList);
            }

            // insert service type
            if (!CollectionUtils.isEmpty(toInsertServiceTypeList)) {
                sdSymptomMapper.createSymptomMapping(toInsertServiceTypeList, symptomCode, createby);
            }
        }
    }

    @Override
    public List<SdSymptomBean> getAllSymptomList() {
        List<SdSymptomEntity> entityList = sdSymptomMapper.getAllSymptomList();
        return buildSymptomBeanList(entityList);
    }

    private List<SdSymptomBean> buildSymptomBeanList(List<SdSymptomEntity> entityList) {

        List<SdSymptomBean> beanList = new LinkedList<>();
        if (CollectionUtils.isNotEmpty(entityList)) {
            for (SdSymptomEntity entity : entityList) {
                SdSymptomBean bean = new SdSymptomBean();
                symptomBeanPopulator.populate(entity, bean);
                beanList.add(bean);
            }
        }

        return beanList;
    }
}
