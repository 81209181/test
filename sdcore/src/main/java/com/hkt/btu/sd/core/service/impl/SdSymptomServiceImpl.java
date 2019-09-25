package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdServiceTypeEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomMappingEntity;
import com.hkt.btu.sd.core.dao.mapper.SdSymptomMapper;
import com.hkt.btu.sd.core.service.SdSymptomService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomBean;
import com.hkt.btu.sd.core.service.populator.SdServiceTypeBeanPopulator;
import com.hkt.btu.sd.core.service.populator.SdSymptomBeanPopulator;
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
    public void createSymptom(String symptomCode,String symptomGroupCode,String symptomDescription) {
        sdSymptomMapper.createSymptom(symptomCode,symptomGroupCode,symptomDescription,userService.getCurrentUserUserId());
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
    public void createSymptomMapping(String serviceTypeCode, String symptomGroupCode) {
        sdSymptomMapper.createSymptomMapping(serviceTypeCode,symptomGroupCode,userService.getCurrentUserUserId());
    }

    @Override
    public boolean checkSymptomMapping(String serviceTypeCode, String symptomGroupCode) {
        Optional<SdSymptomMappingEntity> entity = Optional.ofNullable(sdSymptomMapper.getSymptomMapping(serviceTypeCode, symptomGroupCode));
        return entity.isPresent();
    }

    @Override
    public void deleteSymptomMapping(String serviceTypeCode, String symptomGroupCode) {
        sdSymptomMapper.deleteSymptomMapping(serviceTypeCode,symptomGroupCode);
    }

    @Override
    public boolean checkSymptom(String symptomCode) {
        Optional<SdSymptomEntity> entity = Optional.ofNullable(sdSymptomMapper.getSymptomBySymptomCode(symptomCode));
        return entity.isPresent();
    }
}
