package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.exception.InsufficientAuthorityException;
import com.hkt.btu.sd.core.service.SdSymptomService;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomMappingBean;
import com.hkt.btu.sd.facade.SdSymptomFacade;
import com.hkt.btu.sd.facade.data.EditResultData;
import com.hkt.btu.sd.facade.data.SdServiceTypeData;
import com.hkt.btu.sd.facade.data.SdSymptomData;
import com.hkt.btu.sd.facade.data.SdSymptomMappingData;
import com.hkt.btu.sd.facade.populator.SdServiceTypeDataPopulator;
import com.hkt.btu.sd.facade.populator.SdSymptomDataPopulator;
import com.hkt.btu.sd.facade.populator.SdSymptomMappingDataPopulator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class SdSymptomFacadeImpl implements SdSymptomFacade {
    private static final Logger LOG = LogManager.getLogger(SdSymptomFacadeImpl.class);

    @Resource(name = "sdSymptomService")
    SdSymptomService sdSymptomService;

    @Resource(name = "symptomDataPopulator")
    SdSymptomDataPopulator symptomDataPopulator;

    @Resource(name = "serviceTypeDataPopulator")
    SdServiceTypeDataPopulator serviceTypeDataPopulator;

    @Resource(name = "symptomMappingDataPopulator")
    SdSymptomMappingDataPopulator symptomMappingDataPopulator;

    @Override
    public List<SdSymptomData> getSymptomGroupList() {
        List<SdSymptomBean> beanList = sdSymptomService.getSymptomGroupList();
        if (CollectionUtils.isEmpty(beanList)) {
            return null;
        }

        List<SdSymptomData> dataList = new LinkedList<>();
        for (SdSymptomBean bean : beanList) {
            SdSymptomData data = new SdSymptomData();
            symptomDataPopulator.populate(bean, data);
            dataList.add(data);
        }

        return dataList;
    }

    @Override
    public String createSymptom(String symptomCode, String symptomGroupCode, String symptomDescription) {
        if (StringUtils.isEmpty(symptomCode)) {
            return "Empty Symptom Code.";
        } else if (StringUtils.isEmpty(symptomGroupCode)) {
            return "Empty Symptom Group Code.";
        } else if (StringUtils.isEmpty(symptomDescription)) {
            return "Empty Symptom Description.";
        }

        try {
            sdSymptomService.createSymptom(symptomCode, symptomGroupCode, symptomDescription);
        } catch (DuplicateKeyException e){
            return "Symptom Code already exists.";
        }

        return null;
    }

    @Override
    public List<SdServiceTypeData> getServiceTypeList() {
        List<SdServiceTypeBean> beanList = sdSymptomService.getServiceTypeList();
        if (CollectionUtils.isEmpty(beanList)) {
            return null;
        }

        List<SdServiceTypeData> dataList = new LinkedList<>();
        for (SdServiceTypeBean bean : beanList) {
            SdServiceTypeData data = new SdServiceTypeData();
            serviceTypeDataPopulator.populate(bean, data);
            dataList.add(data);
        }

        return dataList;
    }

    @Override
    public List<SdSymptomData> getAllSymptom() {
        List<SdSymptomBean> beanList = sdSymptomService.getAllSymptom();
        if (CollectionUtils.isEmpty(beanList)) {
            return null;
        }

        List<SdSymptomData> dataList = new LinkedList<>();
        for (SdSymptomBean bean : beanList) {
            SdSymptomData data = new SdSymptomData();
            symptomDataPopulator.populate(bean, data);
            dataList.add(data);
        }

        return dataList;
    }

    @Override
    public SdSymptomData getSymptomBySymptomCode(String symptomCode) {
        SdSymptomBean bean = sdSymptomService.getSymptomBySymptomCode(symptomCode);
        if (bean == null) {
            return null;
        }
        SdSymptomData data = new SdSymptomData();
        symptomDataPopulator.populate(bean, data);
        return data;
    }

    @Override
    public String editSymptomMapping(SdSymptomMappingData symptomMappingData) {
        String symptomCode = symptomMappingData.getSymptomCode();
        List<String> serviceTypeList = symptomMappingData.getServiceTypeList();

        try {
            sdSymptomService.deleteSymptomMapping(symptomCode);
            serviceTypeList.forEach(serviceTypeCode -> {
                sdSymptomService.createSymptomMapping(serviceTypeCode, symptomCode);
            });
        } catch (Exception e){
            LOG.error(e.getMessage());
            return "Edit failed.";
        }

        return null;
    }

    @Override
    public EditResultData getSymptomMapping(String symptomCode) {
        List<String> results;
        try {
            List<SdSymptomMappingBean> userRoleBeanList = sdSymptomService.getSymptomMapping(symptomCode);
            if (CollectionUtils.isEmpty(userRoleBeanList)) {
                return EditResultData.error("Symptom Mapping not found.");
            }
            results = userRoleBeanList.stream().map(bean -> {
                SdSymptomMappingData data = new SdSymptomMappingData();
                symptomMappingDataPopulator.populate(bean, data);
                return data;
            }).collect(Collectors.toList())
                    .stream()
                    .map(SdSymptomMappingData::getServiceTypeCode)
                    .collect(Collectors.toList());
        } catch (InsufficientAuthorityException e) {
            return EditResultData.error(e.getMessage());
        }

        return EditResultData.dataList(results);
    }
}